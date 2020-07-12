package net.apptronic.core.features.provider

import net.apptronic.core.component.di.dependencyDescriptor
import kotlin.reflect.KClass

inline fun <reified T : Any, reified K : Any> dataProviderDescriptor() = dataProviderDescriptor(T::class, K::class)

fun <T : Any, K : Any> dataProviderDescriptor(type: KClass<T>, keyType: KClass<K>) = DataProviderDescriptor(type, keyType)

data class DataProviderDescriptor<T : Any, K : Any> internal constructor(
        val type: KClass<T>, val keyType: KClass<K>
) {
    val providerDescriptor = dependencyDescriptor<DataProvider<T>>()
    val keyDescriptor = dependencyDescriptor(keyType)
}