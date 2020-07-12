package net.apptronic.core.features.service

import net.apptronic.core.component.di.dependencyDescriptor
import kotlin.reflect.KClass

inline fun <reified T : Any, reified R : Any> serviceDescriptor(): ServiceDescriptor<T, R> {
    return serviceDescriptor(T::class, R::class)
}

fun <T : Any, R : Any> serviceDescriptor(requestType: KClass<T>, responseType: KClass<R>): ServiceDescriptor<T, R> {
    return ServiceDescriptor(requestType, responseType)
}

data class ServiceDescriptor<T : Any, R : Any> internal constructor(val requestType: KClass<T>, val responseType: KClass<R>) {
    val holderDescriptor = dependencyDescriptor<ServiceDispatcher<T, R>>()
}