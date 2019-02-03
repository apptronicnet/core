package net.apptronic.common.core.component.di

import net.apptronic.common.core.base.AtomicEntity

abstract class ObjectProvider<TypeDeclaration : Any>(
    internal val key: ObjectKey
) {

    internal var recycler: (TypeDeclaration) -> Unit = {}

    abstract fun provide(context: FactoryContext): TypeDeclaration

    protected fun recycle(instance: TypeDeclaration) {
        recycler.invoke(instance)
        if (instance is AutoRecycling) {
            instance.onAutoRecycle()
        }
    }

}

internal abstract class ObjectBuilderProvider<TypeDeclaration : Any> internal constructor(
    key: ObjectKey,
    internal val builder: FactoryContext.() -> TypeDeclaration
) : ObjectProvider<TypeDeclaration>(key) {

}

internal fun <TypeDeclaration : Any> singleProvider(
    key: ObjectKey,
    builder: FactoryContext.() -> TypeDeclaration
): ObjectProvider<TypeDeclaration> {
    return SingleProvider(key, builder)
}

internal fun <TypeDeclaration : Any> factoryProvider(
    key: ObjectKey,
    builder: FactoryContext.() -> TypeDeclaration
): ObjectProvider<TypeDeclaration> {
    return FactoryProvider(key, builder)
}

internal fun <TypeDeclaration : Any> castProvider(
    key: ObjectKey
): ObjectProvider<TypeDeclaration> {
    return CastProvider(key)
}

private class SingleProvider<TypeDeclaration : Any>(
    key: ObjectKey,
    builder: FactoryContext.() -> TypeDeclaration
) : ObjectBuilderProvider<TypeDeclaration>(key, builder) {

    private val entity = AtomicEntity<TypeDeclaration?>(null)

    override fun provide(context: FactoryContext): TypeDeclaration {
        return entity.perform {
            if (get() == null) {
                set(builder.invoke(context))
            }
            val instance = get()!!
            /**
             * Single instance recycled on exit declared context stage
             */
            context.localLifecycleStage.doOnExit {
                recycle(instance)
            }
            instance
        }
    }

}

private class FactoryProvider<TypeDeclaration : Any>(
    key: ObjectKey,
    builder: FactoryContext.() -> TypeDeclaration
) : ObjectBuilderProvider<TypeDeclaration>(key, builder) {

    override fun provide(context: FactoryContext): TypeDeclaration {
        val instance = builder.invoke(context)
        /**
         * Factory instance recycled when on exit from caller stage
         */
        context.callerLifecycleStage.doOnExit {
            recycle(instance)
        }
        return instance
    }

}

private class CastProvider<TypeDeclaration : Any>(
    key: ObjectKey
) : ObjectProvider<TypeDeclaration>(key) {

    override fun provide(context: FactoryContext): TypeDeclaration {
        return context.inject(key.clazz, key.name) as TypeDeclaration
    }

}