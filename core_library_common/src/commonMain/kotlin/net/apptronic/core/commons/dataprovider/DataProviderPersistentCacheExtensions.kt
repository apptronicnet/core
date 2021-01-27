package net.apptronic.core.commons.dataprovider

import net.apptronic.core.commons.dataprovider.cache.DataProviderCachePersistence
import net.apptronic.core.context.di.ModuleDefinition
import net.apptronic.core.context.di.ProviderDefinition
import net.apptronic.core.context.di.SingleScope

/**
 * Register [DataProviderCachePersistence] for used with [DataProvider] registered using [DataProviderDescriptor]
 * and managed by [SingleScope].
 */
fun <K : Any, T : Any> ModuleDefinition.dataProviderCachePersistence(
    descriptor: DataProviderDescriptor<K, T>,
    initOnLoad: Boolean = false,
    builder: SingleScope.() -> DataProviderCachePersistence<K, T>
): ProviderDefinition<DataProviderCachePersistence<K, T>> {
    return single(descriptor.persistenceDescriptor, initOnLoad, builder)
}