package net.apptronic.core.commons.service

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.coroutines.lifecycleCoroutineScope
import net.apptronic.core.context.di.DependencyDescriptor
import net.apptronic.core.context.lifecycle.enterStage
import net.apptronic.core.context.lifecycle.exitStage

internal class ServiceDispatcherImpl<T : Any, R : Any>(
        context: Context,
        private val serviceInstanceDescriptor: DependencyDescriptor<Service<T, R>>
) : Component(context), ServiceDispatcher<T, R> {

    private inner class PendingRequest(val request: T) {
        val responseDeferred = CompletableDeferred<R>()
    }

    private val pendingRequests = mutableListOf<PendingRequest>()

    override suspend fun sendRequest(request: T): R {
        return nextRequest(request).responseDeferred.await()
    }

    override fun postRequest(request: T) {
        nextRequest(request)
    }

    private fun nextRequest(request: T): PendingRequest {
        val pending = PendingRequest(request)
        pendingRequests.add(pending)
        enterStage(STAGE_SERVICE_RUNNING)
        return pending
    }

    init {
        onEnterStage(STAGE_SERVICE_RUNNING) {
            val service = inject(serviceInstanceDescriptor)
            val coroutineScope = lifecycleCoroutineScope
            coroutineScope.launch {
                while (pendingRequests.isNotEmpty()) {
                    val next = pendingRequests.removeAt(0)
                    try {
                        val response: R = service.onNext(next.request)
                        next.responseDeferred.complete(response)
                    } catch (e: Exception) {
                        service.onError(next.request, e)
                        next.responseDeferred.completeExceptionally(e)
                    }
                }
                exitStage(STAGE_SERVICE_RUNNING)
            }
        }
    }

}