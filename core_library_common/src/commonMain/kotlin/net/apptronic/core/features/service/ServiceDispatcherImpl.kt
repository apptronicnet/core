package net.apptronic.core.features.service

import kotlinx.coroutines.CompletableDeferred
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.ContextDefinition
import net.apptronic.core.component.context.close
import net.apptronic.core.component.coroutines.coroutineLaunchers
import net.apptronic.core.component.extensions.BaseComponent

internal class ServiceDispatcherImpl<T : Any, R : Any>(
        context: Context,
        val serviceContextDefinition: ContextDefinition<Context>,
        private val builder: (Context) -> Service<T, R>
) : BaseComponent(context), ServiceDispatcher<T, R> {

    private inner class PendingRequest(val request: T) {
        val responseDeferred = CompletableDeferred<R>()
    }

    private val pendingRequests = mutableListOf<PendingRequest>()
    private var service: Service<T, R>? = null
    private var coroutineLauncher = coroutineLaunchers().scoped

    override suspend fun sendRequest(request: T): R {
        return nextRequest(request).responseDeferred.await()
    }

    override fun postRequest(request: T) {
        nextRequest(request)
    }

    private fun nextRequest(request: T): PendingRequest {
        val pending = PendingRequest(request)
        pendingRequests.add(pending)
        runService()
        return pending
    }

    private fun runService() {
        if (service == null) {
            val serviceContext = serviceContextDefinition.createContext(context)
            service = builder(serviceContext).also {
                runService(it)
            }
        }
    }

    private fun runService(service: Service<T, R>) {
        coroutineLauncher.launch {
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
            service.context.close()
            this@ServiceDispatcherImpl.service = null
        }
    }

}