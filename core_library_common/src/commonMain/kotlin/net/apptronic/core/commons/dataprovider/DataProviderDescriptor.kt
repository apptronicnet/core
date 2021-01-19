package net.apptronic.core.commons.dataprovider

import net.apptronic.core.commons.dataprovider.cache.DataProviderCache
import net.apptronic.core.commons.dataprovider.cache.DataProviderCachePersistence
import net.apptronic.core.commons.dataprovider.cache.DataProviderCacheProcessor
import net.apptronic.core.context.di.SharedScopeManager
import net.apptronic.core.context.di.dependencyDescriptor
import kotlin.reflect.KClass

inline fun <reified K : Any, reified T : Any> dataProviderDescriptor() = dataProviderDescriptor(K::class, T::class)

fun <K : Any, T : Any> dataProviderDescriptor(keyType: KClass<K>, type: KClass<T>) =
    DataProviderDescriptor(keyType, type)

data class DataProviderDescriptor<K : Any, T : Any> internal constructor(
    val keyType: KClass<K>, val type: KClass<T>,
) {
    internal val dispatcherDescriptor = dependencyDescriptor<DataProviderDispatcher<K, T>>()
    internal val holderDescriptor = dependencyDescriptor<DataProviderHolder<K, T>>()
    internal val keyDescriptor = dependencyDescriptor(keyType)
    internal val cacheDescriptor = dependencyDescriptor<DataProviderCache<K, T>>()
    internal val persistenceDescriptor = dependencyDescriptor<DataProviderCachePersistence<K, T>>()
    internal val providerInstanceDescriptor = dependencyDescriptor<DataProvider<K, T>>()
    internal val scopeManagerDescriptor = dependencyDescriptor<SharedScopeManager>()
    internal val cacheProcessorDescriptor = dependencyDescriptor<DataProviderCacheProcessor<K, T>>()
}