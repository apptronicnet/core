package net.apptronic.core.component.di

import kotlinx.coroutines.delay
import net.apptronic.core.base.elapsedRealtimeMillis
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.coroutineLauncherLocal

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

internal abstract class ObjectBuilderProvider<TypeDeclaration : Any, BuilderScope : ObjectBuilderScope> internal constructor(
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
        return entity ?: run {
            val scope = SingleScope(definitionContext, dispatcher)
            val created: TypeDeclaration = builder.invoke(scope)
            scope.autoRecycle(created)
            entity = created
            definitionContext.lifecycle.doOnTerminate {
                scope.finalize()
                entity = null
            }
            created
        }
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
        return instance
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

    private val coroutineLauncher = context.coroutineLauncherLocal()

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
        val share = shares[parameters] ?: run {
            val scope = SharedScope(definitionContext, dispatcher, parameters)
            val instance = builder.invoke(scope)
            scope.autoRecycle(instance)
            val share = SharedInstance(instance, scope)
            shares[parameters] = share
            share
        }
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
            coroutineLauncher.launch {
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
        return dispatcher.inject(objectKey) as TypeDeclaration
    }

}