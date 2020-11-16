package net.apptronic.core.commons.service

import kotlinx.coroutines.launch
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.coroutines.contextCoroutineScope
import net.apptronic.core.entity.commons.property
import net.apptronic.core.entity.commons.value
import net.apptronic.core.entity.function.map
import net.apptronic.core.entity.operators.decrement
import net.apptronic.core.entity.operators.increment

internal class ServiceClientImpl<T : Any, R : Any>(
        context: Context,
        private val dispatcher: ServiceDispatcher<T, R>
) : Component(context), ServiceClient<T, R> {

    private val requestsInProgress = value(0)

    override val isProcessing = property(dispatcher.observeProcessingRequests(context), false)

    override val isProcessingInContext = requestsInProgress.map { it > 0 }

    override fun postRequest(request: T) {
        contextCoroutineScope.launch {
            val pendingRequest = dispatcher.nextRequest(request)
            try {
                requestsInProgress.increment()
                pendingRequest.await() // ignore response
            } catch (e: Exception) {
                // ignore
            } finally {
                requestsInProgress.decrement()
            }
        }
    }

    override suspend fun sendRequest(request: T): R {
        try {
            requestsInProgress.increment()
            return dispatcher.nextRequest(request).await()
        } finally {
            requestsInProgress.decrement()
        }
    }

}