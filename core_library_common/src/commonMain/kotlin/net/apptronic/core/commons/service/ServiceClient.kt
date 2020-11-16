package net.apptronic.core.commons.service

import net.apptronic.core.context.Context
import net.apptronic.core.context.component.IComponent
import net.apptronic.core.entity.base.Property

interface ServiceClient<T : Any, R : Any> : IComponent {

    /**
     * Represents is [Service] now processing any requests
     */
    val isProcessing: Property<Boolean>

    /**
     * Represents is [Service] now processing any requests in current [Context]
     */
    val isProcessingInContext: Property<Boolean>

    /**
     * Send request to service and wait for the response. Service may throw an [Exception] here, but it still be also
     * processed by service itself if supported.
     */
    suspend fun sendRequest(request: T): R

    /**
     * Send request to service and do not wait for response. If service will thrown an [Exception] it will be ignored
     * here but will be processed by service itself if supported.
     */
    fun postRequest(request: T)

}
