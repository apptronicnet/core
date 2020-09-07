package net.apptronic.core.commons.service

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.di.ModuleDefinition
import net.apptronic.core.component.di.SharedScope
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
        builder: SharedScope.() -> Service<T, R>) {
    shared(descriptor.serviceInstanceDescriptor, builder = builder)
    single(descriptor.holderDescriptor) {
        ServiceDispatcherImpl(scopedContext(ServiceDispatcherLifecycle), descriptor.serviceInstanceDescriptor)
    }
}

fun <T : Any, R : Any> Contextual.injectService(descriptor: ServiceDescriptor<T, R>): ServiceDispatcher<T, R> {
    return inject(descriptor.holderDescriptor)
}