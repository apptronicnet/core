package net.apptronic.core.commons.service

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.coroutines.lifecycleCoroutineScope
import net.apptronic.core.context.di.DependencyDescriptor
import net.apptronic.core.context.lifecycle.enterStage
import net.apptronic.core.context.lifecycle.exitStage
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.commons.value

internal class ServiceDispatcher<T : Any, R : Any>(
        context: Context,
        private val serviceInstanceDescriptor: DependencyDescriptor<Service<T, R>>
) : Component(context) {

    interface PendingRequest<T, R> {
        val request: T
        suspend fun await(): R
    }

    private inner class PendingRequestImpl(override val request: T) : PendingRequest<T, R> {
        val responseDeferred = CompletableDeferred<R>()
        override suspend fun await(): R {
            return responseDeferred.await()
        }
    }

    private val pendingRequests = mutableListOf<PendingRequestImpl>()

    private val isProcessingRequests = value(false)

    fun observeProcessingRequests(targetContext: Context): Entity<Boolean> {
        return isProcessingRequests.switchContext(targetContext)
    }

    fun nextRequest(request: T): PendingRequest<T, R> {
        val pending = PendingRequestImpl(request)
        pendingRequests.add(pending)
        enterStage(STAGE_SERVICE_RUNNING)
        return pending
    }

    init {
        onEnterStage(STAGE_SERVICE_RUNNING) {
            isProcessingRequests.set(true)
            onExit {
                isProcessingRequests.set(false)
            }
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