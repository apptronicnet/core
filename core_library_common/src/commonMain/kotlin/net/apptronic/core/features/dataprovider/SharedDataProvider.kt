package net.apptronic.core.features.dataprovider

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.di.ModuleDefinition
import net.apptronic.core.component.di.SharedScope
import net.apptronic.core.component.di.SingleScope
import net.apptronic.core.component.di.parameters
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.inject
import net.apptronic.core.features.cache.CacheComponent

fun <T : Any> Contextual.injectData(descriptor: DataProviderDescriptor<T, Unit>): Entity<T> {
    return inject(descriptor.holderDescriptor, parameters { add(descriptor.keyDescriptor, Unit) }).provideData(context)
}

fun <T : Any, K : Any> Contextual.injectData(descriptor: DataProviderDescriptor<T, K>, key: K): Entity<T> {
    return inject(descriptor.holderDescriptor, parameters { add(descriptor.keyDescriptor, key) }).provideData(context)
}

fun <T : Any, K : Any> ModuleDefinition.sharedCache(
        descriptor: DataProviderDescriptor<T, K>,
        builder: SingleScope.() -> CacheComponent<T, K>
) {
    single(descriptor.cacheDescriptor, builder)
}

inline fun <T : Any, reified K : Any> ModuleDefinition.sharedDataProvider(
        descriptor: DataProviderDescriptor<T, K>,
        fallbackCount: Int = 0,
        fallbackLifetime: Long = 0,
        noinline builder: SharedScope.(K) -> DataProvider<T, K>
) {
    shared(descriptor.holderDescriptor, fallbackCount, fallbackLifetime) {
        val key: K = provided(descriptor.keyDescriptor)
        val provider = builder(key)
        val cache = optional(descriptor.cacheDescriptor)
        DataProviderHolder<T, K>(scopedContext(), key, provider, cache)
    }
}