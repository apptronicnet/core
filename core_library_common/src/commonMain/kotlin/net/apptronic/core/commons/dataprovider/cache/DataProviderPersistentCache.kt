package net.apptronic.core.commons.dataprovider.cache

import net.apptronic.core.base.subject.ValueHolder

interface DataProviderPersistentCache<K, T> {

    suspend fun save(key: K, value: T)

    suspend fun load(key: K): ValueHolder<T>?

}