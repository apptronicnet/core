package net.apptronic.core.component.di

import net.apptronic.core.component.context.Context

internal sealed class ObjectProvider<TypeDeclaration>(
        objectKey: ObjectKey
) {

    abstract val typeName: String

    val recyclers = mutableListOf<RecyclerMethod<TypeDeclaration>>()
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

    protected fun recycle(instance: TypeDeclaration) {
        recyclers.forEach { it.invoke(instance) }
        if (instance is AutoRecycling) {
            instance.onAutoRecycle()
        }
    }

}

internal abstract class ObjectBuilderProvider<TypeDeclaration, BuilderScope : ObjectBuilderScope> internal constructor(
        objectKey: ObjectKey,
        internal val builder: BuilderMethod<TypeDeclaration, BuilderScope>
) : ObjectProvider<TypeDeclaration>(objectKey) {

}

internal fun <TypeDeclaration> singleProvider(
        objectKey: ObjectKey,
        builder: BuilderMethod<TypeDeclaration, SingleScope>
): ObjectProvider<TypeDeclaration> {
    return SingleProvider(objectKey, builder)
}

internal fun <TypeDeclaration> sharedProvider(
        objectKey: ObjectKey,
        builder: BuilderMethod<TypeDeclaration, SharedScope>
): ObjectProvider<TypeDeclaration> {
    return SharedProvider(objectKey, builder)
}

internal fun <TypeDeclaration> factoryProvider(
        objectKey: ObjectKey,
        builder: BuilderMethod<TypeDeclaration, FactoryScope>
): ObjectProvider<TypeDeclaration> {
    return FactoryProvider(objectKey, builder)
}

internal fun <TypeDeclaration> bindProvider(
        objectKey: ObjectKey
): ObjectProvider<TypeDeclaration> {
    return BindProvider(objectKey)
}

private class SingleProvider<TypeDeclaration>(
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
            entity = created
            definitionContext.lifecycle.doOnTerminate {
                recycle(created)
                scope.finalize()
                entity = null
            }
            created
        }
    }

}

private class FactoryProvider<TypeDeclaration>(
        objectKey: ObjectKey,
        builder: BuilderMethod<TypeDeclaration, FactoryScope>
) : ObjectBuilderProvider<TypeDeclaration, FactoryScope>(objectKey, builder) {

    override val typeName: String = "factory"

    override fun provide(definitionContext: Context, dispatcher: DependencyDispatcher, searchSpec: SearchSpec): TypeDeclaration {
        val injectionContext = searchSpec.context
        val scope = FactoryScope(definitionContext, injectionContext, dispatcher, searchSpec.params)
        val instance = builder.invoke(scope)
        /**
         * Factory instance recycled when on exit from caller stage
         */
        injectionContext.lifecycle.onExitFromActiveStage {
            recycle(instance)
            scope.finalize()
        }
        return instance
    }

}

private class SharedProvider<TypeDeclaration>(
        objectKey: ObjectKey,
        builder: BuilderMethod<TypeDeclaration, SharedScope>
) : ObjectBuilderProvider<TypeDeclaration, SharedScope>(objectKey, builder) {

    override val typeName: String = "shared"

    private inner class SharedInstance(
            val instance: TypeDeclaration,
            val scope: SharedScope
    ) {
        var shareCount: Int = 0
        fun recycle() {
            scope.finalize()
        }
    }

    private val shares = mutableMapOf<Parameters, SharedInstance>()

    override fun provide(definitionContext: Context, dispatcher: DependencyDispatcher, searchSpec: SearchSpec): TypeDeclaration {
        val providedContext = searchSpec.context
        val parameters = searchSpec.params
        val share = shares[parameters] ?: run {
            val scope = SharedScope(definitionContext, dispatcher, parameters)
            val instance = builder.invoke(scope)
            val share = SharedInstance(instance, scope)
            shares[parameters] = share
            share
        }
        share.shareCount++
        providedContext.lifecycle.onExitFromActiveStage {
            share.shareCount--
            if (share.shareCount == 0) {
                shares.remove(parameters)
                recycle(share.instance)
                share.recycle()
            }
        }
        return share.instance
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