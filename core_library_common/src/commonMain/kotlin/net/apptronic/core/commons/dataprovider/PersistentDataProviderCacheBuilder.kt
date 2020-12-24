package net.apptronic.core.commons.dataprovider

import net.apptronic.core.commons.dataprovider.cache.CachePersistence
import net.apptronic.core.commons.dataprovider.cache.DataProviderCache
import net.apptronic.core.commons.dataprovider.cache.PersistentDataProviderCache
import net.apptronic.core.context.di.Scope

fun <K, T> Scope.persistentCache(
    persistence: CachePersistence<K, T>
): DataProviderCache<K, T> {
    return PersistentDataProviderCache(scopedContext(), persistence)
}