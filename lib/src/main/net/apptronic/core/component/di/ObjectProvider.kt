package net.apptronic.core.component.di

import net.apptronic.core.base.AtomicEntity
import net.apptronic.core.component.context.Context

enum class RecycleOn {

    OnTerminate {
        override fun registerRecycle(context: Context, recycle: () -> Unit) {
            context.getLifecycle().doOnTerminate(recycle)
        }
    },
    OnExitCurrentStage {
        override fun registerRecycle(context: Context, recycle: () -> Unit) {
            context.getLifecycle().onExitFromActiveStage(recycle)
        }
    };

    internal abstract fun registerRecycle(context: Context, recycle: () -> Unit)

    companion object {
        val DEFAULT = OnExitCurrentStage
    }

}

internal abstract class ObjectProvider<TypeDeclaration>(
    objectKey: ObjectKey
) {

    val recyclers = mutableListOf<RecyclerMethod<TypeDeclaration>>()
    private val objectKeys = mutableListOf<ObjectKey>()

    internal fun addMapping(additionalKeys: Iterable<ObjectKey>) {
        objectKeys.addAll(additionalKeys)
    }

    init {
        objectKeys.add(objectKey)
    }

    internal fun isMatch(key: ObjectKey): Boolean {
        return objectKeys.any { it == key }
    }

    abstract fun provide(
        definitionContext: Context,
        provider: DependencyProvider,
        searchSpec: SearchSpec
    ): TypeDeclaration

    protected fun recycle(instance: TypeDeclaration) {
        recyclers.forEach { it.invoke(instance) }
        if (instance is AutoRecycling) {
            instance.onAutoRecycle()
        }
    }

}

internal abstract class ObjectBuilderProvider<TypeDeclaration, BuilderContext : ObjectBuilderContext> internal constructor(
    objectKey: ObjectKey,
    internal val builder: BuilderMethod<TypeDeclaration, BuilderContext>,
    val recycleOn: RecycleOn
) : ObjectProvider<TypeDeclaration>(objectKey) {

}

internal fun <TypeDeclaration> singleProvider(
    objectKey: ObjectKey,
    builder: BuilderMethod<TypeDeclaration, SingleContext>,
    recycleOn: RecycleOn
): ObjectProvider<TypeDeclaration> {
    return SingleProvider(objectKey, builder, recycleOn)
}

internal fun <TypeDeclaration> factoryProvider(
    objectKey: ObjectKey,
    builder: BuilderMethod<TypeDeclaration, FactoryContext>,
    recycleOn: RecycleOn
): ObjectProvider<TypeDeclaration> {
    return FactoryProvider(objectKey, builder, recycleOn)
}

internal fun <TypeDeclaration> bindProvider(
    objectKey: ObjectKey
): ObjectProvider<TypeDeclaration> {
    return BindProvider(objectKey)
}

private class SingleProvider<TypeDeclaration>(
    objectKey: ObjectKey,
    builder: BuilderMethod<TypeDeclaration, SingleContext>,
    recycleOn: RecycleOn
) : ObjectBuilderProvider<TypeDeclaration, SingleContext>(objectKey, builder, recycleOn) {

    private val entity = AtomicEntity<TypeDeclaration?>(null)

    override fun provide(
        definitionContext: Context,
        provider: DependencyProvider,
        searchSpec: SearchSpec
    ): TypeDeclaration {
        val singleContext = SingleContext(definitionContext, provider, searchSpec.params)
        return entity.perform {
            if (get() == null) {
                val created = builder.invoke(singleContext)
                set(created)
                /**
                 * Single instance recycled on exit declared context stage
                 */
                recycleOn.registerRecycle(definitionContext) {
                    perform {
                        // make instance null. It will be recreated when called again
                        recycle(created)
                        set(null)
                    }
                }
            }
            val instance = get()!!
            instance
        }
    }

}

private class FactoryProvider<TypeDeclaration>(
    objectKey: ObjectKey,
    builder: BuilderMethod<TypeDeclaration, FactoryContext>,
    recycleOn: RecycleOn
) : ObjectBuilderProvider<TypeDeclaration, FactoryContext>(objectKey, builder, recycleOn) {

    override fun provide(
        definitionContext: Context,
        provider: DependencyProvider,
        searchSpec: SearchSpec
    ): TypeDeclaration {
        val factoryContext =
            FactoryContext(definitionContext, searchSpec.context, provider, searchSpec.params)
        val instance = builder.invoke(factoryContext)
        /**
         * Factory instance recycled when on exit from caller stage
         */
        recycleOn.registerRecycle(searchSpec.context) {
            recycle(instance)
        }
        return instance
    }

}

private class BindProvider<TypeDeclaration>(
    private val objectKey: ObjectKey
) : ObjectProvider<TypeDeclaration>(objectKey) {

    override fun provide(
        definitionContext: Context,
        provider: DependencyProvider,
        searchSpec: SearchSpec
    ): TypeDeclaration {
        return provider.inject(objectKey) as TypeDeclaration
    }

}