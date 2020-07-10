package net.apptronic.core.features.service

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.ContextDefinition
import net.apptronic.core.component.context.EmptyContext
import net.apptronic.core.component.di.DependencyDescriptor
import net.apptronic.core.component.di.ModuleDefinition
import net.apptronic.core.component.di.dependencyDescriptor

inline fun <reified T> serviceDependencyDescriptor(): DependencyDescriptor<ServiceDispatcher<T>> {
    return dependencyDescriptor<ServiceDispatcher<T>>()
}

interface ServiceDispatcher<T> {

    fun sendRequest(request: T)

}

fun <T> ModuleDefinition.service(
        descriptor: DependencyDescriptor<ServiceDispatcher<T>>,
        serviceContextDefinition: ContextDefinition<Context> = EmptyContext,
        builder: (Context) -> Service<T>) {
    single(descriptor) {
        ServiceHolder(context, serviceContextDefinition, builder)
    }
}