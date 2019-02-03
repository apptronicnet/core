package net.apptronic.common.core.component.di

import net.apptronic.common.core.base.AtomicEntity

internal abstract class ObjectProvider<T : Any> internal constructor(
    internal val key: ObjectKey,
    internal val builder: FactoryContext.() -> T
) {

    internal var recycler: (T) -> Unit = {}

    abstract fun provide(context: FactoryContext): T

}

internal fun <T : Any> singleProvider(
    key: ObjectKey,
    builder: FactoryContext.() -> T
): ObjectProvider<T> {
    return SingleProvider(key, builder)
}

internal fun <T : Any> factoryProvider(
    key: ObjectKey,
    builder: FactoryContext.() -> T
): ObjectProvider<T> {
    return FactoryProvider(key, builder)
}

private class SingleProvider<T : Any>(
    key: ObjectKey,
    builder: FactoryContext.() -> T
) : ObjectProvider<T>(key, builder) {

    private val entity = AtomicEntity<T?>(null)

    override fun provide(context: FactoryContext): T {
        return entity.perform {
            if (get() == null) {
                set(builder.invoke(context))
            }
            get()!!
        }
    }

}

private class FactoryProvider<T : Any>(
    key: ObjectKey,
    builder: FactoryContext.() -> T
) : ObjectProvider<T>(key, builder) {

    override fun provide(context: FactoryContext): T {
        return builder.invoke(context)
    }

}