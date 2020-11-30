package net.apptronic.core.context.di

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.apptronic.core.base.elapsedRealtimeMillis
import net.apptronic.core.context.Context
import net.apptronic.core.context.coroutines.contextCoroutineScope

internal sealed class ObjectProvider<TypeDeclaration>(
        objectKey: ObjectKey
) {

    abstract val typeName: String

    private val objectKeys = mutableListOf<ObjectKey>()

    internal fun addMapping(additionalKeys: Iterable<ObjectKey>) {
        objectKeys.addAll(additionalKeys)
    }

    internal fun getMappings(): List<ObjectKey> {
        return objectKeys
    }

    init {
        objectKeys.add(objectKey)
    }

    internal fun isMatch(key: ObjectKey): Boolean {
        return objectKeys.any { it == key }
    }

    abstract fun provide(
            definitionContext: Context,
            dispatcher: DependencyDispatcher,
            searchSpec: SearchSpec
    ): TypeDeclaration

}

internal abstract class ObjectBuilderProvider<TypeDeclaration : Any, BuilderScope : Scope> internal constructor(
        objectKey: ObjectKey,
        internal val builder: BuilderMethod<TypeDeclaration, BuilderScope>
) : ObjectProvider<TypeDeclaration>(objectKey) {

}

internal fun <TypeDeclaration : Any> singleProvider(
        objectKey: ObjectKey,
        builder: BuilderMethod<TypeDeclaration, SingleScope>
): ObjectProvider<TypeDeclaration> {
    return SingleProvider(objectKey, builder)
}

internal fun <TypeDeclaration : Any> sharedDataProvider(
        objectKey: ObjectKey,
        builder: BuilderMethod<TypeDeclaration, SharedScope>,
        context: Context,
        fallbackCount: Int,
        fallbackLifetime: Long
): ObjectProvider<TypeDeclaration> {
    return SharedProvider(objectKey, builder, context, fallbackCount, fallbackLifetime)
}

internal fun <TypeDeclaration : Any> factoryProvider(
        objectKey: ObjectKey,
        builder: BuilderMethod<TypeDeclaration, FactoryScope>
): ObjectProvider<TypeDeclaration> {
    return FactoryProvider(objectKey, builder)
}

internal fun <TypeDeclaration : Any> bindProvider(
        objectKey: ObjectKey
): ObjectProvider<TypeDeclaration> {
    return BindProvider(objectKey)
}

internal fun <TypeDeclaration : Any> instanceProvider(
        objectKey: ObjectKey, instance: TypeDeclaration
): ObjectProvider<TypeDeclaration> {
    return InstanceProvider(objectKey, instance)
}

private class SingleProvider<TypeDeclaration : Any>(
        objectKey: ObjectKey,
        builder: BuilderMethod<TypeDeclaration, SingleScope>
) : ObjectBuilderProvider<TypeDeclaration, SingleScope>(objectKey, builder) {

    override val typeName: String = "single"

    private var entity: TypeDeclaration? = null

    override fun provide(
            definitionContext: Context,
            dispatcher: DependencyDispatcher,
            searchSpec: SearchSpec
    ): TypeDeclaration {
        return entity ?: createInstance(definitionContext, dispatcher)
    }

    private fun createInstance(
            definitionContext: Context,
            dispatcher: DependencyDispatcher
    ): TypeDeclaration {
        val scope = SingleScope(definitionContext, dispatcher)
        val instance: TypeDeclaration = builder.invoke(scope)
        scope.autoRecycle(instance)
        val processed = definitionContext.plugins.nextProvide(definitionContext, instance)
        entity = processed
        definitionContext.lifecycle.doOnTerminate {
            scope.finalize()
            entity = null
        }
        return processed
    }

}

private class FactoryProvider<TypeDeclaration : Any>(
        objectKey: ObjectKey,
        builder: BuilderMethod<TypeDeclaration, FactoryScope>
) : ObjectBuilderProvider<TypeDeclaration, FactoryScope>(objectKey, builder) {

    override val typeName: String = "factory"

    override fun provide(definitionContext: Context, dispatcher: DependencyDispatcher, searchSpec: SearchSpec): TypeDeclaration {
        val injectionContext = searchSpec.context
        val scope = FactoryScope(definitionContext, injectionContext, dispatcher, searchSpec.params)
        val instance = builder.invoke(scope)
        scope.autoRecycle(instance)
        /**
         * Factory instance recycled when on exit from caller stage
         */
        injectionContext.lifecycle.onExitFromActiveStage {
            scope.finalize()
        }
        return definitionContext.plugins.nextProvide(definitionContext, instance)
    }

}

private class SharedProvider<TypeDeclaration : Any>(
        objectKey: ObjectKey,
        builder: BuilderMethod<TypeDeclaration, SharedScope>,
        private val context: Context,
        private val fallbackCount: Int,
        private val fallbackLifetime: Long
) : ObjectBuilderProvider<TypeDeclaration, SharedScope>(objectKey, builder) {

    override val typeName: String = "shared"

    private val coroutineScope = context.contextCoroutineScope

    private class SharedInstance<T>(
            val instance: T,
            val scope: SharedScope
    ) {
        var shareCount: Int = 0
        fun recycle() {
            scope.finalize()
        }
    }

    private class UnusedShare<T>(
            val parameters: Parameters,
            val share: SharedInstance<T>,
            val unusedFrom: Long
    )

    private val shares = mutableMapOf<Parameters, SharedInstance<TypeDeclaration>>()
    private val unused = mutableListOf<UnusedShare<TypeDeclaration>>()

    override fun provide(definitionContext: Context, dispatcher: DependencyDispatcher, searchSpec: SearchSpec): TypeDeclaration {
        val providedContext = searchSpec.context
        val parameters = searchSpec.params
        val share = shares[parameters] ?: createInstance(
                definitionContext, dispatcher, parameters
        )
        share.shareCount++
        providedContext.lifecycle.onExitFromActiveStage {
            share.shareCount--
            if (share.shareCount == 0) {
                shares.remove(parameters)
                unused(parameters, share)
            }
        }
        return share.instance
    }

    private fun createInstance(
            definitionContext: Context, dispatcher: DependencyDispatcher, parameters: Parameters
    ): SharedInstance<TypeDeclaration> {
        val scope = SharedScope(definitionContext, dispatcher, parameters)
        val instance = builder.invoke(scope)
        scope.autoRecycle(instance)
        val processed = definitionContext.plugins.nextProvide(definitionContext, instance)
        val share = SharedInstance(processed, scope)
        shares[parameters] = share
        return share
    }

    private fun unused(parameters: Parameters, share: SharedInstance<TypeDeclaration>) {
        if (fallbackCount <= 0 || fallbackLifetime <= 0) {
            share.recycle()
        } else {
            unused.add(UnusedShare(parameters, share, elapsedRealtimeMillis()))
            clearUnused()
        }
    }

    fun clearUnused() {
        val timestamp = elapsedRealtimeMillis()
        unused.removeAll {
            if (timestamp - it.unusedFrom > fallbackCount) {
                it.share.recycle()
                true
            } else false
        }
        while (unused.isNotEmpty() && unused.size > fallbackCount) {
            unused.removeAt(0).share.recycle()
        }
        scheduleAutoClearUnused()
    }

    private fun scheduleAutoClearUnused() {
        if (unused.isNotEmpty()) {
            val lifetimeInUnused = elapsedRealtimeMillis() - unused[0].unusedFrom
            val delayTime = fallbackLifetime - lifetimeInUnused
            coroutineScope.launch {
                if (delayTime > 0) {
                    delay(delayTime)
                }
                clearUnused()
            }
        }
    }

}

private class BindProvider<TypeDeclaration>(
        private val objectKey: ObjectKey
) : ObjectProvider<TypeDeclaration>(objectKey) {

    override val typeName: String = "bind"

    override fun provide(definitionContext: Context, dispatcher: DependencyDispatcher, searchSpec: SearchSpec): TypeDeclaration {
        return dispatcher.inject(objectKey, emptyParameters()) as TypeDeclaration
    }

}

private class InstanceProvider<TypeDeclaration>(
        private val objectKey: ObjectKey,
        private val instance: TypeDeclaration
) : ObjectProvider<TypeDeclaration>(objectKey) {

    override val typeName: String = "instance"

    override fun provide(definitionContext: Context, dispatcher: DependencyDispatcher, searchSpec: SearchSpec): TypeDeclaration {
        return instance
    }

}