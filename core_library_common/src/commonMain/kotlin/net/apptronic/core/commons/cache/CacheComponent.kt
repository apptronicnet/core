package net.apptronic.core.commons.cache

import kotlinx.coroutines.Job
import net.apptronic.core.UnderDevelopment
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component

@UnderDevelopment
abstract class CacheComponent<K, T>(context: Context) : Component(context) {

    abstract operator fun set(key: K, value: T)

    abstract fun get(key: K): ValueHolder<T>?

    open fun releaseKey(key: K) {
        // implement by subclasses if needed
    }

    open fun getAsync(key: K, target: (T) -> Unit): Job? {
        get(key)?.let {
            target(it.value)
        }
        return null
    }

}