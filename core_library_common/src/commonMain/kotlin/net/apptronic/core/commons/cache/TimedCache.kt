package net.apptronic.core.commons.cache

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.apptronic.core.UnderDevelopment
import net.apptronic.core.base.elapsedRealtimeMillis
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.coroutines.contextCoroutineScope
import net.apptronic.core.context.doAsync
import kotlin.math.max

@UnderDevelopment
class TimedCache<K, T>(
        context: Context,
        private val maxFallbackCount: Int = 32,
        private val fallbackLifetimeMillis: Long = 60000L, // 1 minute
) : Component(context), Cache<K, T> {

    private val simpleCache = SimpleCache<K, T>(maxFallbackCount, true)
    private val expirationTimes = mutableMapOf<K, Long>()

    private var cleanupJob: Job? = null

    override fun get(key: K): ValueHolder<T>? {
        expirationTimes.remove(key)
        cleanup()
        return simpleCache[key]
    }

    override fun getAsync(key: K, target: (T) -> Unit): Job? {
        get(key)?.let {
            target(it.value)
        }
        return null
    }

    override fun releaseKey(key: K) {
        super.releaseKey(key)
        expirationTimes[key] = elapsedRealtimeMillis() + fallbackLifetimeMillis
        cleanup()
    }

    override fun set(key: K, value: T) {
        simpleCache[key] = value
        cleanup()
    }

    private fun cleanup() {
        cleanupJob?.cancel()
        cleanupJob = null
        val cleanupCount = expirationTimes.size - maxFallbackCount
        if (cleanupCount > 0) {
            expirationTimes.entries.sortedBy { it.value }.take(cleanupCount).forEach {
                expirationTimes.remove(it.key)
            }
        }
        val currentTime = elapsedRealtimeMillis()
        expirationTimes.filter { it.value <= currentTime }.forEach {
            expirationTimes.remove(it.key)
            simpleCache.releaseKey(it.key)
        }
        val firstExpiration = expirationTimes.minByOrNull { it.value }
        if (firstExpiration != null) {
            val diff = max(0L, firstExpiration.value - currentTime)
            cleanupJob = contextCoroutineScope.launch {
                delay(diff)
                doAsync {
                    cleanup()
                }
            }
        }
    }

}