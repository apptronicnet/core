package net.apptronic.core.commons.cache

import kotlinx.coroutines.Job
import net.apptronic.core.UnderDevelopment
import net.apptronic.core.base.elapsedRealtimeMillis
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context

@UnderDevelopment
class SimpleCache<T, K>(
        context: Context,
        private val maxCount: Int = 32,
        private val targetCache: CacheComponent<T, K>? = null
) : CacheComponent<T, K>(context) {

    private inner class CacheEntry(val value: T, var lastUsed: Long = elapsedRealtimeMillis())

    private val map = mutableMapOf<K, CacheEntry>()

    override fun get(key: K): ValueHolder<T>? {
        return map[key]?.let {
            it.lastUsed = elapsedRealtimeMillis()
            ValueHolder(it.value)
        } ?: targetCache?.get(key)
    }

    override fun getAsync(key: K, target: (T) -> Unit): Job? {
        val local = get(key)
        return if (local != null) {
            target(local.value)
            null
        } else targetCache?.getAsync(key, target)
    }

    override fun releaseKey(key: K) {
        targetCache?.releaseKey(key)
        super.releaseKey(key)
        cleanup()
    }

    override fun set(key: K, value: T) {
        map[key] = CacheEntry(value)
        cleanup()
        targetCache?.set(key, value)
    }

    private fun cleanup() {
        map.entries.sortedBy {
            it.value.lastUsed
        }.dropLast(maxCount).forEach {
            map.remove(it.key)
        }
    }

}