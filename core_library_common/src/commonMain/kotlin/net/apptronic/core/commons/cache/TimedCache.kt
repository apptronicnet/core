package net.apptronic.core.commons.cache

import kotlinx.coroutines.Job
import net.apptronic.core.UnderDevelopment
import net.apptronic.core.base.elapsedRealtimeMillis
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.context.Context

@UnderDevelopment
class TimedCache<T, K>(
        context: Context,
        private val maxFallbackCount: Int = 32,
        private val fallbackLifetimeMillis: Long = 60000L, // 1 minute
        private val targetCache: CacheComponent<T, K>? = null
) : CacheComponent<T, K>(context) {

    private inner class ExpirationKey(val key: K, val expirationTimestamp: Long) : Comparable<ExpirationKey> {
        override fun compareTo(other: ExpirationKey): Int {
            return expirationTimestamp.compareTo(other.expirationTimestamp)
        }
    }

    private val map = mutableMapOf<K, ValueHolder<T>>()
    private val released = mutableListOf<ExpirationKey>()

    override fun get(key: K): ValueHolder<T>? {
        released.removeAll {
            it.key == key
        }
        return map[key] ?: targetCache?.get(key)
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
        if (map.containsKey(key)) {
            released.add(ExpirationKey(key, elapsedRealtimeMillis() + fallbackLifetimeMillis))
            released.sort()
        }
        cleanup()
    }

    override fun set(key: K, value: T) {
        map[key] = ValueHolder(value)
        cleanup()
        targetCache?.set(key, value)
    }

    private fun cleanup() {
        val now = elapsedRealtimeMillis()
        released.removeAll {
            if (it.expirationTimestamp < now) {
                map.remove(it.key)
                true
            } else {
                false
            }
        }
        while (released.size > maxFallbackCount && released.size > 0) {
            released.removeAt(0).let {
                map.remove(it.key)
            }
        }
    }

}