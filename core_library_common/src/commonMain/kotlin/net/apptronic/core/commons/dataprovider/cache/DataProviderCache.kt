package net.apptronic.core.commons.dataprovider.cache

import kotlinx.coroutines.Job
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.commons.dataprovider.DataProvider

/**
 * Cache designed for usage with [DataProvider]
 */
interface DataProviderCache<K, T> {

    /**
     * Set value to cache.
     *
     * @return [ValueHolder] containing value or null is value is not present in cache
     */
    operator fun set(key: K, value: T)

    /**
     * Called to instantly read cached value (if exists)
     *
     * @return [ValueHolder] containing value or null is value is not present in cache
     */
    operator fun get(key: K): ValueHolder<T>?

    /**
     * Called when instant read was unsuccessful.
     * @return optional [Job] object which represents read process and can be cancelled in case when read is no
     * more needed
     */
    fun getAsync(key: K, target: (T) -> Unit): Job?

    /**
     * Notify that cached value is no more used. Allows cache to optimize it's internals to handle only values
     * which are used now (for example clean up memory).
     */
    fun releaseKey(key: K)

}