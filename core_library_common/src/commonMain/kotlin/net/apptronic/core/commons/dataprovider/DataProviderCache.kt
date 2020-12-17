package net.apptronic.core.commons.dataprovider

import net.apptronic.core.commons.cache.Cache
import net.apptronic.core.context.di.ModuleDefinition
import net.apptronic.core.context.di.SingleProviderDefinition
import net.apptronic.core.context.di.SingleScope


/**
 * Register [Cache] for used with [DataProvider] registered using [DataProviderDescriptor] managed by [SingleScope].
 */
fun <K : Any, T : Any> ModuleDefinition.dataProviderCache(
        descriptor: DataProviderDescriptor<K, T>,
        builder: SingleScope.() -> Cache<K, T>
): SingleProviderDefinition<Cache<K, T>> {
    return single(descriptor.cacheDescriptor, builder)
}