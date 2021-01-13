package net.apptronic.core.commons.dataprovider

import net.apptronic.core.commons.dataprovider.cache.DataProviderCache
import net.apptronic.core.commons.dataprovider.cache.DataProviderCachePersistence
import net.apptronic.core.commons.dataprovider.cache.PersistentDataProviderCache
import net.apptronic.core.context.di.Scope

fun <K, T> Scope.persistentCache(
    persistence: DataProviderCachePersistence<K, T>
): DataProviderCache<K, T> {
    return PersistentDataProviderCache(scopedContext(), persistence)
}