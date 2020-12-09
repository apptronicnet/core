package net.apptronic.core.commons.cache

import kotlinx.coroutines.Job
import net.apptronic.core.UnderDevelopment
import net.apptronic.core.base.elapsedRealtimeMillis
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.context.Context

@UnderDevelopment
class SimpleCache<K, T>(
        context: Context,
        private val maxCount: Int = 32
) : CacheComponent<K, T>(context) {

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

    override fun releaseKey(key: K) {
        super.releaseKey(key)
        cleanup()
    }

    override operator fun set(key: K, value: T) {
        map[key] = CacheEntry(value)
        cleanup()
    }

    private fun cleanup() {
        map.entries.sortedBy {
            it.value.lastUsed
        }.dropLast(maxCount).forEach {
            map.remove(it.key)
        }
    }

}