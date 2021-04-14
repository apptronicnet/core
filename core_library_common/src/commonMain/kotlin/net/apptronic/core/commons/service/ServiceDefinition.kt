package net.apptronic.core.commons.service

import net.apptronic.core.context.Contextual
import net.apptronic.core.context.di.ModuleDefinition
import net.apptronic.core.context.di.SharedScope

fun <T : Any, R : Any> ModuleDefinition.service(
        descriptor: ServiceDescriptor<T, R>,
        builder: SharedScope.() -> Service<T, R>) {
    shared(descriptor.serviceInstanceDescriptor, builder = builder)
    single(descriptor.holderDescriptor) {
        ServiceDispatcher(scopedContext(), descriptor.serviceInstanceDescriptor)
    }
}

fun <T : Any, R : Any> Contextual.injectService(descriptor: ServiceDescriptor<T, R>): ServiceClient<T, R> {
    val dispatcher = dependencyProvider.inject(descriptor.holderDescriptor)
    return ServiceClientImpl(context, dispatcher)
}