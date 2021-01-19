package net.apptronic.core.commons.dataprovider.cache

import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.base.subject.asValueHolder

/**
 * Allows to process cache before save or load
 */
interface DataProviderCacheProcessor<K, T> {

    /**
     * Process [value] for key [key]. Return [ValueHolder] with value which will be stored in [DataProviderCache]
     * or null to store no value.
     */
    fun processSet(key: K, value: T): ValueHolder<T>? {
        return value.asValueHolder()
    }

    /**
     * Process [value] for key [key]. Return [ValueHolder] with value which will be returned from [DataProviderCache]
     * or null to store no value.
     */
    fun processGet(key: K, value: T): ValueHolder<T>? {
        return value.asValueHolder()
    }

}

fun <K, T> DataProviderCache<K, T>.processedWith(processor: DataProviderCacheProcessor<K, T>?): DataProviderCache<K, T> {
    return if (processor == null) this else ProcessedDataProviderCache(this, processor)
}