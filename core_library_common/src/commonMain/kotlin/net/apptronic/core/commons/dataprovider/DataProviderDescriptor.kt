package net.apptronic.core.commons.dataprovider

import net.apptronic.core.commons.cache.CacheComponent
import net.apptronic.core.context.di.dependencyDescriptor
import kotlin.reflect.KClass

inline fun <reified T : Any, reified K : Any> dataProviderDescriptor() = dataProviderDescriptor(T::class, K::class)

fun <T : Any, K : Any> dataProviderDescriptor(type: KClass<T>, keyType: KClass<K>) = DataProviderDescriptor(type, keyType)

data class DataProviderDescriptor<T : Any, K : Any> internal constructor(
        val type: KClass<T>, val keyType: KClass<K>
) {
    internal val holderDescriptor = dependencyDescriptor<DataProviderHolder<T, K>>()
    internal val keyDescriptor = dependencyDescriptor(keyType)
    internal val cacheDescriptor = dependencyDescriptor<CacheComponent<T, K>>()
}