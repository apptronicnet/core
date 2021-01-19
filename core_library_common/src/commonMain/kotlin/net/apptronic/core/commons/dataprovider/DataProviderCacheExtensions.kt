package net.apptronic.core.commons.dataprovider

import net.apptronic.core.commons.dataprovider.cache.*
import net.apptronic.core.context.di.ModuleDefinition
import net.apptronic.core.context.di.ProviderDefinition
import net.apptronic.core.context.di.SingleScope

/**
 * Register [DataProviderCache] for used with [DataProvider] registered using [DataProviderDescriptor] managed by [SingleScope].
 */
fun <K : Any, T : Any> ModuleDefinition.dataProviderCache(
    descriptor: DataProviderDescriptor<K, T>,
    initOnLoad: Boolean = false,
    builder: SingleScope.() -> DataProviderCache<K, T>
): ProviderDefinition<DataProviderCache<K, T>> {
    return single(descriptor.cacheDescriptor, initOnLoad) {
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
    initOnLoad: Boolean = false,
    builder: SimpleDataProviderCacheBuilder<K, T>.() -> Unit
): ProviderDefinition<DataProviderCache<K, T>> {
    return single(descriptor.cacheDescriptor, initOnLoad) {
        simpleCache(builder)
    }
}

fun <K : Any, T : Any> ModuleDefinition.dataProviderTimedCache(
    descriptor: DataProviderDescriptor<K, T>,
    initOnLoad: Boolean = false,
    builder: TimedDataProviderCacheBuilder<K, T>.() -> Unit
): ProviderDefinition<DataProviderCache<K, T>> {
    return single(descriptor.cacheDescriptor, initOnLoad) {
        timedCache(builder)
    }
}

fun <K : Any, T : Any> ModuleDefinition.dataProviderCacheProcessor(
    descriptor: DataProviderDescriptor<K, T>,
    initOnLoad: Boolean = false,
    builder: SingleScope.() -> DataProviderCacheProcessor<K, T>
): ProviderDefinition<DataProviderCacheProcessor<K, T>> {
    return single(descriptor.cacheProcessorDescriptor, initOnLoad, builder = builder)
}
