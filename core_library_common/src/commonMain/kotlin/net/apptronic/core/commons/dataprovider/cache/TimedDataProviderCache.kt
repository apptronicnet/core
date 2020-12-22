package net.apptronic.core.commons.dataprovider.cache

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.apptronic.core.UnderDevelopment
import net.apptronic.core.base.elapsedRealtimeMillis
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.coroutines.contextCoroutineScope

@UnderDevelopment
class TimedDataProviderCache<K, T>(
        context: Context,
        private val maxSize: Int = 32,
        private val sizeFunction: (T) -> Int = { 1 },
        private val expirationTime: Long = 60000L, // 1 minute
) : Component(context), DataProviderCache<K, T> {

    private inner class CacheEntry(val value: T, var expiresAt: Long = Long.MAX_VALUE)

    private val map = mutableMapOf<K, CacheEntry>()

    override operator fun get(key: K): ValueHolder<T>? {
        return map[key]?.let {
            it.expiresAt = Long.MAX_VALUE
            cleanup()
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
        map[key]?.let {
            it.expiresAt = elapsedRealtimeMillis() + expirationTime
        }
        cleanup()
    }

    private var cleanupJob: Job? = null

    private fun cleanup() {
        cleanupJob?.cancel()
        cleanupJob = null

        map.trimToSizeFromMin({ sizeFunction(it.value) }, maxSize, { it.expiresAt })

        val currentTime = elapsedRealtimeMillis()
        do {
            val firstExpiration = map.minByOrNull { it.value.expiresAt }
            if (firstExpiration != null) {
                val diff = firstExpiration.value.expiresAt - currentTime
                if (diff > 0) {
                    cleanupJob = contextCoroutineScope.launch {
                        delay(diff)
                        cleanup()
                    }
                    return
                } else {
                    map.remove(firstExpiration.key)
                }
            }
        } while (firstExpiration != null)
    }

}