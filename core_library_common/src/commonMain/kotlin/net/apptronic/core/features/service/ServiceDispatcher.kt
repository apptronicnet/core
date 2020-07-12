package net.apptronic.core.features.service

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.ContextDefinition
import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.EmptyContext
import net.apptronic.core.component.di.ModuleDefinition
import net.apptronic.core.component.inject

interface ServiceDispatcher<T : Any, R : Any> {

    /**
     * Send request to service wait for response. Service may throw an exception
     */
    suspend fun sendRequest(request: T): R

    /**
     * Send request to service and do not wait for response
     */
    fun postRequest(request: T)

}

fun <T : Any, R : Any> ModuleDefinition.service(
        descriptor: ServiceDescriptor<T, R>,
        serviceContextDefinition: ContextDefinition<Context> = EmptyContext,
        builder: (Context) -> Service<T, R>) {
    single(descriptor.holderDescriptor) {
        ServiceDispatcherImpl(definitionContext(), serviceContextDefinition, builder)
    }
}

fun <T : Any, R : Any> Contextual.injectService(descriptor: ServiceDescriptor<T, R>): ServiceDispatcher<T, R> {
    return inject(descriptor.holderDescriptor)
}