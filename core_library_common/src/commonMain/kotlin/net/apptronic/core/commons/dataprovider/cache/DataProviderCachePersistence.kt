package net.apptronic.core.commons.dataprovider.cache

import net.apptronic.core.base.subject.ValueHolder

/**
 * Persistence layer for use with [PersistentProxyDataProviderCache]. Allows to simply add persistence layer to
 * in-memory cache.
 */
interface DataProviderCachePersistence<K, T> {

    /**
     * Save cached value to persistent storage
     */
    suspend fun save(key: K, value: T)

    /**
     * Load cached value from persistent storage
     *
     * @return [ValueHolder] containing value or null is value is not present in cache
     */
    suspend fun load(key: K): ValueHolder<T>?

    /**
     * Force clear all cache
     */
    fun clear() {
        // implement by subclasses if needed
    }

}