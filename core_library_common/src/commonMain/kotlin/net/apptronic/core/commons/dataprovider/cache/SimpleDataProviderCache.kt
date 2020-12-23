package net.apptronic.core.commons.dataprovider.cache

import kotlinx.coroutines.Job
import net.apptronic.core.base.elapsedRealtimeMillis
import net.apptronic.core.base.subject.ValueHolder


/**
 * Cache which holds fixed amount of cached data and clears automatically when cache size exceeds limit
 * beginning from entries which is not used maximum amount of time.
 */
class SimpleDataProviderCache<K, T>(
    private val maxSize: Int,
    private val sizeFunction: (T) -> Int
) : DataProviderCache<K, T> {

    private inner class CacheEntry(val value: T, var lastUsed: Long = Long.MAX_VALUE)

    private val map = mutableMapOf<K, CacheEntry>()

    override operator fun get(key: K): ValueHolder<T>? {
        return map[key]?.let {
            it.lastUsed = Long.MAX_VALUE
            ValueHolder(it.value)
        }
    }

    override fun getAsync(key: K, target: (T) -> Unit): Job? {
        get(key)?.let {
            target(it.value)
        }
        return null
    }

    override operator fun set(key: K, value: T) {
        map[key] = CacheEntry(value)
        cleanup()
    }

    override fun releaseKey(key: K) {
        map[key]?.let {
            it.lastUsed = elapsedRealtimeMillis()
        }
    }

    private fun cleanup() {
        map.trimToSizeFromMin({ sizeFunction(it.value) }, maxSize, { it.lastUsed })
    }

}