package net.apptronic.core.commons.dataprovider

import net.apptronic.core.commons.cache.CacheComponent
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.di.ModuleDefinition
import net.apptronic.core.context.di.SharedScope
import net.apptronic.core.context.di.SingleScope
import net.apptronic.core.context.di.parameters

/**
 * Inject data of type [T] using [descriptor] with [Unit] type.
 */
fun <T : Any> Contextual.injectData(descriptor: DataProviderDescriptor<Unit, T>): DataProviderClient<T> {
    val holder = dependencyProvider.inject(descriptor.holderDescriptor, parameters { add(descriptor.keyDescriptor, Unit) })
    return DataProviderClientImpl(context, holder)
}

/**
 * Inject data of type [T] using [descriptor] with [key].
 */
fun <T : Any, K : Any> Contextual.injectData(descriptor: DataProviderDescriptor<K, T>, key: K): DataProviderClient<T> {
    val holder = dependencyProvider.inject(descriptor.holderDescriptor, parameters { add(descriptor.keyDescriptor, key) })
    return DataProviderClientImpl(context, holder)
}

/**
 * Register [CacheComponent] for used with [DataProvider] registered using [DataProviderDescriptor] managed by [SingleScope].
 */
fun <T : Any, K : Any> ModuleDefinition.sharedCache(
        descriptor: DataProviderDescriptor<T, K>,
        builder: SingleScope.() -> CacheComponent<T, K>
) {
    single(descriptor.cacheDescriptor, builder)
}

/**
 * Add [DataProvider] dependency which managed by [SharedScope],
 *
 * To add cache use [sharedCache] with same [DataProviderDescriptor]
 */
fun <T : Any, K : Any> ModuleDefinition.sharedDataProvider(
        descriptor: DataProviderDescriptor<K, T>,
        fallbackCount: Int = 0,
        fallbackLifetime: Long = 0,
        builder: SharedScope.(K) -> DataProvider<K, T>
) {
    shared(descriptor.holderDescriptor, fallbackCount, fallbackLifetime) {
        val key: K = provided(descriptor.keyDescriptor)
        val provider = builder(key)
        val cache = optional(descriptor.cacheDescriptor)
        DataProviderHolder<K, T>(scopedContext(), key, provider, cache)
    }
}