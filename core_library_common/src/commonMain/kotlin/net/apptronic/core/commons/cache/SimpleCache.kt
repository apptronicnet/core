package net.apptronic.core.commons.cache

import kotlinx.coroutines.Job
import net.apptronic.core.base.elapsedRealtimeMillis
import net.apptronic.core.base.subject.ValueHolder

class SimpleCache<K, T>(
        private val maxCount: Int = 32,
        private val cleanupOnRelease: Boolean = false
) : Cache<K, T> {

    private inner class CacheEntry(val value: T, var lastUsed: Long = elapsedRealtimeMillis())

    private val map = mutableMapOf<K, CacheEntry>()

    override operator fun get(key: K): ValueHolder<T>? {
        return map[key]?.let {
            it.lastUsed = elapsedRealtimeMillis()
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
        super.releaseKey(key)
        if (cleanupOnRelease) {
            map.remove(key)
        }
    }

    private fun cleanup() {
        val countToDrop = map.size - maxCount
        if (countToDrop > 0) {
            map.entries.sortedBy {
                it.value.lastUsed
            }.take(countToDrop).forEach {
                map.remove(it.key)
            }
        }
    }

}