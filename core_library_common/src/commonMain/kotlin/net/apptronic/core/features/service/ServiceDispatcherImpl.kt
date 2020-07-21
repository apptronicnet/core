package net.apptronic.core.features.service

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.lifecycleCoroutineScope
import net.apptronic.core.component.di.DependencyDescriptor
import net.apptronic.core.component.extensions.BaseComponent
import net.apptronic.core.component.inject
import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.component.lifecycle.exitStage

internal class ServiceDispatcherImpl<T : Any, R : Any>(
        context: Context,
        private val serviceInstanceDescriptor: DependencyDescriptor<Service<T, R>>
) : BaseComponent(context), ServiceDispatcher<T, R> {

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