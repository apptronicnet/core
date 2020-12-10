package net.apptronic.core.commons.cache

import kotlinx.coroutines.Job
import net.apptronic.core.UnderDevelopment
import net.apptronic.core.base.subject.ValueHolder

@UnderDevelopment
interface Cache<K, T> {

    operator fun set(key: K, value: T)

    operator fun get(key: K): ValueHolder<T>?

    fun getAsync(key: K, target: (T) -> Unit): Job? {
        get(key)?.let {
            target(it.value)
        }
        return null
    }

    /**
     * Notify that cached value is no more used. Allows cache to optimize it's internals to handle only values
     * which are used now.
     */
    fun releaseKey(key: K) {
        // implement by subclasses if needed
    }

}