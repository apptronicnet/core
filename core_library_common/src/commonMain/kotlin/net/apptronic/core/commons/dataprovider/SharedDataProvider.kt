package net.apptronic.core.commons.dataprovider

import net.apptronic.core.context.Contextual
import net.apptronic.core.context.di.ModuleDefinition
import net.apptronic.core.context.di.SharedScope
import net.apptronic.core.context.di.parameters

/**
 * Inject data of type [T] using [descriptor] with [Unit] type.
 */
fun <T : Any> Contextual.injectData(descriptor: DataProviderDescriptor<Unit, T>): DataProviderProperty<T> {
    val holder = dependencyProvider.inject(descriptor.holderDescriptor, parameters { add(descriptor.keyDescriptor, Unit) })
    return DataProviderPropertyImpl(context, holder)
}

/**
 * Inject data of type [T] using [descriptor] with [key].
 */
fun <T : Any, K : Any> Contextual.injectData(descriptor: DataProviderDescriptor<K, T>, key: K): DataProviderProperty<T> {
    val holder = dependencyProvider.inject(descriptor.holderDescriptor, parameters { add(descriptor.keyDescriptor, key) })
    return DataProviderPropertyImpl(context, holder)
}


/**
 * Add [DataProvider] dependency which managed by [SharedScope]
 *
 * To add cache use [dataProviderCache] with same [DataProviderDescriptor]
 */
fun <K : Any, T : Any> ModuleDefinition.sharedDataProvider(
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