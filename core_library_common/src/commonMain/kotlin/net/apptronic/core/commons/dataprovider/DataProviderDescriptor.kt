package net.apptronic.core.commons.dataprovider

import net.apptronic.core.commons.cache.Cache
import net.apptronic.core.context.di.dependencyDescriptor
import kotlin.reflect.KClass

inline fun <reified K : Any, reified T : Any> dataProviderDescriptor() = dataProviderDescriptor(K::class, T::class)

fun <K : Any, T : Any> dataProviderDescriptor(keyType: KClass<K>, type: KClass<T>) = DataProviderDescriptor(keyType, type)

data class DataProviderDescriptor<K : Any, T : Any> internal constructor(
        val keyType: KClass<K>, val type: KClass<T>,
) {
    internal val holderDescriptor = dependencyDescriptor<DataProviderHolder<K, T>>()
    val keyDescriptor = dependencyDescriptor(keyType)
    val cacheDescriptor = dependencyDescriptor<Cache<K, T>>()
}