package net.apptronic.core.component.di

import net.apptronic.core.base.AtomicEntity

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

    abstract fun provide(context: FactoryContext): TypeDeclaration

    protected fun recycle(instance: TypeDeclaration) {
        recyclers.forEach { it.invoke(instance) }
        if (instance is AutoRecycling) {
            instance.onAutoRecycle()
        }
    }

}

internal abstract class ObjectBuilderProvider<TypeDeclaration> internal constructor(
    objectKey: ObjectKey,
    internal val builder: BuilderMethod<TypeDeclaration>
) : ObjectProvider<TypeDeclaration>(objectKey) {

}

internal fun <TypeDeclaration> singleProvider(
    objectKey: ObjectKey,
    builder: BuilderMethod<TypeDeclaration>
): ObjectProvider<TypeDeclaration> {
    return SingleProvider(objectKey, builder)
}

internal fun <TypeDeclaration> factoryProvider(
    objectKey: ObjectKey,
    builder: BuilderMethod<TypeDeclaration>
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
    builder: BuilderMethod<TypeDeclaration>
) : ObjectBuilderProvider<TypeDeclaration>(objectKey, builder) {

    private val entity = AtomicEntity<TypeDeclaration?>(null)

    override fun provide(context: FactoryContext): TypeDeclaration {
        return entity.perform {
            if (get() == null) {
                val created = builder.invoke(context)
                set(created)
                /**
                 * Single instance recycled on exit declared context stage
                 */
                context.localLifecycleStage.doOnExit {
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
    builder: BuilderMethod<TypeDeclaration>
) : ObjectBuilderProvider<TypeDeclaration>(objectKey, builder) {

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

private class BindProvider<TypeDeclaration>(
    private val objectKey: ObjectKey
) : ObjectProvider<TypeDeclaration>(objectKey) {

    override fun provide(context: FactoryContext): TypeDeclaration {
        /**
         * Cast instance is not recycling as it will be recycled by real provider when required
         */
        return context.inject(objectKey) as TypeDeclaration
    }

}