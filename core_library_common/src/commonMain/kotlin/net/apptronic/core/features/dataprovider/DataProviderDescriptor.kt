package net.apptronic.core.features.dataprovider

import net.apptronic.core.component.di.dependencyDescriptor
import net.apptronic.core.features.cache.CacheComponent
import kotlin.reflect.KClass

inline fun <reified T : Any, reified K : Any> dataProviderDescriptor() = dataProviderDescriptor(T::class, K::class)

fun <T : Any, K : Any> dataProviderDescriptor(type: KClass<T>, keyType: KClass<K>) = DataProviderDescriptor(type, keyType)

data class DataProviderDescriptor<T : Any, K : Any> internal constructor(
        val type: KClass<T>, val keyType: KClass<K>
) {
    val holderDescriptor = dependencyDescriptor<DataProviderHolder<T, K>>()
    val keyDescriptor = dependencyDescriptor(keyType)
    val cacheDescriptor = dependencyDescriptor<CacheComponent<T, K>>()
}