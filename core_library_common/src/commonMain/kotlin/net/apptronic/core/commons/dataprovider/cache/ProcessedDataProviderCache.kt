package net.apptronic.core.commons.dataprovider.cache

import kotlinx.coroutines.Job
import net.apptronic.core.base.subject.ValueHolder

class ProcessedDataProviderCache<K, T>(
    private val targetCache: DataProviderCache<K, T>,
    private val processor: DataProviderCacheProcessor<K, T>
) : DataProviderCache<K, T> {

    override fun get(key: K): ValueHolder<T>? {
        return targetCache[key]?.let {
            processor.processGet(key, it.value)
        }
    }

    override fun getAsync(key: K, target: (T) -> Unit): Job? {
        return targetCache.getAsync(key) {
            processor.processGet(key, it)?.let { processed ->
                target(processed.value)
            }
        }
    }

    override fun set(key: K, value: T) {
        processor.processSet(key, value)?.let {
            targetCache[key] = it.value
        }
    }

    override fun clear() {
        targetCache.clear()
    }

    override fun releaseKey(key: K) {
        targetCache.releaseKey(key)
    }

}