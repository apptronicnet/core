package net.apptronic.core.commons.dataprovider

import net.apptronic.core.commons.dataprovider.cache.*
import net.apptronic.core.context.di.ModuleDefinition
import net.apptronic.core.context.di.SingleProviderDefinition
import net.apptronic.core.context.di.SingleScope

/**
 * Register [DataProviderCache] for used with [DataProvider] registered using [DataProviderDescriptor] managed by [SingleScope].
 */
fun <K : Any, T : Any> ModuleDefinition.dataProviderCache(
    descriptor: DataProviderDescriptor<K, T>,
    builder: SingleScope.() -> DataProviderCache<K, T>
): SingleProviderDefinition<DataProviderCache<K, T>> {
    return single(descriptor.cacheDescriptor) {
        val cache = builder()
        val persistence = optional(descriptor.persistenceDescriptor)
        if (persistence == null) {
            cache
        } else {
            PersistentProxyDataProviderCache(scopedContext(), cache, persistence)
        }
    }
}

fun <K : Any, T : Any> ModuleDefinition.dataProviderSimpleCache(
    descriptor: DataProviderDescriptor<K, T>,
    builder: SimpleDataProviderCacheBuilder<K, T>.() -> Unit
): SingleProviderDefinition<DataProviderCache<K, T>> {
    return single(descriptor.cacheDescriptor) {
        simpleCache(builder)
    }
}

fun <K : Any, T : Any> ModuleDefinition.dataProviderTimedCache(
    descriptor: DataProviderDescriptor<K, T>,
    builder: TimedDataProviderCacheBuilder<K, T>.() -> Unit
): SingleProviderDefinition<DataProviderCache<K, T>> {
    return single(descriptor.cacheDescriptor) {
        timedCache(builder)
    }
}
