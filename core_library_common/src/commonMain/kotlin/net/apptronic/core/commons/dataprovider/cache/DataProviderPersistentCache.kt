package net.apptronic.core.commons.dataprovider.cache

import net.apptronic.core.base.subject.ValueHolder

/**
 * Persistence layer for use with [PersistentProxyDataProviderCache]. Allows to simply add persistence layer to
 * in-memory cache.
 */
interface DataProviderPersistentCache<K, T> {

    suspend fun save(key: K, value: T)

    suspend fun load(key: K): ValueHolder<T>?

}