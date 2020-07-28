package net.apptronic.core.component.di

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.ContextDefinition
import net.apptronic.core.component.context.childContext
import net.apptronic.core.component.context.terminate
import net.apptronic.core.component.lifecycle.BASE_LIFECYCLE
import net.apptronic.core.component.lifecycle.LifecycleDefinition
import kotlin.reflect.KClass

/**
 * Context of creating methods in module definition
 */
abstract class Scope internal constructor(
        protected val definitionContext: Context,
        protected val dependencyDispatcher: DependencyDispatcher,
        protected val parameters: Parameters
) {

    private val recyclers = mutableListOf<RecyclerMethod<*>>()

    inline fun <reified ObjectType : Any> inject(): ObjectType {
        if (ObjectType::class == Context::class) {
            throw IllegalArgumentException("Cannot inject [Context]. Please use definitionContext() or providedContext() instead")
        }
        return inject(ObjectType::class)
    }

    inline fun <reified ObjectType : Any> optional(): ObjectType? {
        if (ObjectType::class == Context::class) {
            throw IllegalArgumentException("Cannot inject [Context]. Please use definitionContext() or providedContext() instead")
        }
        return optional(ObjectType::class)
    }

    fun <ObjectType : Any> inject(
            clazz: KClass<ObjectType>
    ): ObjectType {
        if (clazz == Context::class) {
            throw IllegalArgumentException("Cannot inject [Context]. Please use definitionContext() or providedContext() instead")
        }
        return performInjection(objectKey(clazz))
    }

    fun <ObjectType : Any> optional(
            clazz: KClass<ObjectType>
    ): ObjectType? {
        if (clazz == Context::class) {
            throw IllegalArgumentException("Cannot inject [Context]. Please use definitionContext() or providedContext() instead")
        }
        return optionalInjection(objectKey(clazz))
    }

    fun <ObjectType : Any> inject(
            descriptor: DependencyDescriptor<ObjectType>
    ): ObjectType {
        return performInjection(objectKey(descriptor))
    }

    fun <ObjectType : Any> optional(
            descriptor: DependencyDescriptor<ObjectType>
    ): ObjectType? {
        return optionalInjection(objectKey(descriptor))
    }

    /**
     * Inject context in which this provider is defined.
     */
    fun definitionContext(): Context {
        return definitionContext
    }

    private fun <ObjectType> performInjection(
            objectKey: ObjectKey
    ): ObjectType {
        return parameters.get(objectKey) ?: dependencyDispatcher.inject(objectKey)
    }

    private fun <ObjectType> optionalInjection(
            objectKey: ObjectKey
    ): ObjectType? {
        return parameters.get(objectKey) ?: dependencyDispatcher.optional(objectKey)
    }

    internal abstract val defaultBuilderContext: Context

    private val contexts = mutableListOf<Context>()

    /**
     * Return new instance of [Context] which is bound to this scope and will be automatically cancelled when
     * this scope ends.
     */
    @Suppress("UNUSED_PARAMETER")
    fun <T : Context> scopedContext(
            contextDefinition: ContextDefinition<T>): T {
        val scopedContext = defaultBuilderContext.childContext(contextDefinition)
        contexts.add(scopedContext)
        return scopedContext
    }

    @Suppress("UNUSED_PARAMETER")
    fun scopedContext(
            lifecycleDefinition: LifecycleDefinition = BASE_LIFECYCLE,
            builder: Context.() -> Unit = {}
    ): Context {
        val scopedContext = defaultBuilderContext.childContext(lifecycleDefinition, builder)
        contexts.add(scopedContext)
        return scopedContext
    }

    fun autoRecycle(isAutoRecycle: Any) {
        if (isAutoRecycle is AutoRecycling) {
            isAutoRecycle.onRecycle {
                it.onAutoRecycle()
            }
        }
    }

    fun <T> T.onRecycle(recycler: (T) -> Unit): T {
        recyclers.add(RecyclerMethod(this, recycler))
        return this
    }

    internal fun finalize() {
        recyclers.forEach {
            it.executeRecycle()
        }
        contexts.forEach {
            it.terminate()
        }
    }

}

class SingleScope internal constructor(
        definitionContext: Context,
        dependencyDispatcher: DependencyDispatcher
) : Scope(definitionContext, dependencyDispatcher, emptyParameters()) {

    override val defaultBuilderContext: Context = definitionContext

}

abstract class ParametersScope(
        definitionContext: Context,
        dependencyDispatcher: DependencyDispatcher,
        parameters: Parameters
) : Scope(definitionContext, dependencyDispatcher, parameters) {

    /**
     * Request injection from injection context. Allows to override instances in child context or parameters
     */
    inline fun <reified ObjectType : Any> provided(): ObjectType {
        return provided(ObjectType::class)
    }

    /**
     * Request injection from injection context. Allows to override instances in child context or parameters
     */
    fun <ObjectType : Any> provided(
            clazz: KClass<ObjectType>
    ): ObjectType {
        if (clazz == Context::class) {
            throw  IllegalArgumentException("Cannot inject [Context]. Please use definitionContext() or providedContext() instead")
        }
        return performProvide(objectKey(clazz))
    }

    /**
     * Request injection from injection context. Allows to override descriptors in child context or parameters
     */
    fun <ObjectType : Any> provided(
            descriptor: DependencyDescriptor<ObjectType>
    ): ObjectType {
        return performProvide(objectKey(descriptor))
    }

    internal abstract fun <ObjectType> performProvide(
            objectKey: ObjectKey
    ): ObjectType
}

class FactoryScope internal constructor(
        definitionContext: Context,
        private val injectionContext: Context,
        dependencyDispatcher: DependencyDispatcher,
        parameters: Parameters
) : ParametersScope(definitionContext, dependencyDispatcher, parameters) {

    private val injectionDispatcher = injectionContext.dependencyDispatcher

    /**
     * Inject context in which injection performed
     */
    fun providedContext(): Context {
        return injectionContext
    }

    override fun <ObjectType> performProvide(objectKey: ObjectKey): ObjectType {
        return parameters.get(objectKey) ?: injectionDispatcher.inject(objectKey)
    }

    override val defaultBuilderContext: Context = injectionContext

}

class SharedScope internal constructor(
        definitionContext: Context,
        dependencyDispatcher: DependencyDispatcher,
        parameters: Parameters
) : ParametersScope(definitionContext, dependencyDispatcher, parameters) {

    override fun <ObjectType> performProvide(objectKey: ObjectKey): ObjectType {
        return parameters.get(objectKey)
                ?: throw InjectionFailedException("Cannot provide $objectKey: this is missing in provided parameters")
    }

    override val defaultBuilderContext: Context = definitionContext

}