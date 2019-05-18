package net.apptronic.core.component.di

import net.apptronic.core.component.context.Context
import kotlin.reflect.KClass

/**
 * Context of creating methods in module definition
 */
abstract class ObjectBuilderContext internal constructor(
    protected val definitionContext: Context,
    protected val dependencyProvider: DependencyProvider,
    protected val parameters: Parameters
) {

    inline fun <reified ObjectType : Any> inject(): ObjectType {
        if (ObjectType::class == Context::class) {
            throw  IllegalArgumentException("Cannot inject [Context]. Please use definitionContext() or providedContext() instead")
        }
        return inject(ObjectType::class)
    }

    fun <ObjectType : Any> inject(
        clazz: KClass<ObjectType>
    ): ObjectType {
        if (clazz == Context::class) {
            throw  IllegalArgumentException("Cannot inject [Context]. Please use definitionContext() or providedContext() instead")
        }
        return performInjection(objectKey(clazz))
    }

    fun <ObjectType : Any> inject(
        descriptor: Descriptor<ObjectType>
    ): ObjectType {
        return performInjection(objectKey(descriptor))
    }

    /**
     * Inject context in which this provider is defined.
     */
    fun definitionContext(): Context {
        return definitionContext
    }

    internal fun <ObjectType> performInjection(
        objectKey: ObjectKey
    ): ObjectType {
        return parameters.get(objectKey) ?: dependencyProvider.inject(objectKey)
    }

}

class FactoryContext(
    definitionContext: Context,
    private val injectionContext: Context,
    dependencyProvider: DependencyProvider,
    parameters: Parameters
) : ObjectBuilderContext(definitionContext, dependencyProvider, parameters) {

    private val requestorProvider = injectionContext.getProvider()

    /**
     * Request injection from injection context. Allows to override instances in child context
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
     * Request injection from injection context. Allows to override descriptors in child context
     */
    fun <ObjectType : Any> provided(
        descriptor: Descriptor<ObjectType>
    ): ObjectType {
        return performProvide(objectKey(descriptor))
    }

    /**
     * Inject context in which injection performed
     */
    fun providedContext(): Context {
        return injectionContext
    }

    internal fun <ObjectType> performProvide(
        objectKey: ObjectKey
    ): ObjectType {
        return parameters.get(objectKey) ?: requestorProvider.inject(objectKey)
    }

}

class SingleContext(
    definitionContext: Context,
    dependencyProvider: DependencyProvider,
    parameters: Parameters
) : ObjectBuilderContext(definitionContext, dependencyProvider, parameters)