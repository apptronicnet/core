package net.apptronic.core.commons.dataprovider

import net.apptronic.core.commons.dataprovider.cache.DataProviderPersistentCache
import net.apptronic.core.context.di.ModuleDefinition
import net.apptronic.core.context.di.SingleProviderDefinition
import net.apptronic.core.context.di.SingleScope

/**
 * Register [DataProviderPersistentCache] for used with [DataProvider] registered using [DataProviderDescriptor]
 * and managed by [SingleScope].
 */
fun <K : Any, T : Any> ModuleDefinition.dataProviderPersistentCache(
    descriptor: DataProviderDescriptor<K, T>,
    builder: SingleScope.() -> DataProviderPersistentCache<K, T>
): SingleProviderDefinition<DataProviderPersistentCache<K, T>> {
    return single(descriptor.persistenceDescriptor, builder)
}