package net.apptronic.core.features.service

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.ContextDefinition
import net.apptronic.core.component.context.close
import net.apptronic.core.component.coroutines.coroutineLaunchers
import net.apptronic.core.component.extensions.BaseComponent

internal class ServiceHolder<T>(
        context: Context,
        val serviceContextDefinition: ContextDefinition<Context>,
        private val builder: (Context) -> Service<T>
) : BaseComponent(context), ServiceDispatcher<T> {

    private val pendingRequests = mutableListOf<T>()
    private var service: Service<T>? = null
    private var coroutineLauncher = coroutineLaunchers().scoped

    override fun sendRequest(request: T) {
        pendingRequests.add(request)
        postRequest()
    }

    private fun postRequest() {
        if (service == null) {
            val serviceContext = serviceContextDefinition.createContext(context)
            service = builder(serviceContext).also {
                runService(it)
            }
        }
    }

    private fun runService(service: Service<T>) {
        coroutineLauncher.launch {
            while (pendingRequests.isNotEmpty()) {
                val next = pendingRequests.removeAt(0)
                service.onNext(next)
            }
            service.context.close()
            this@ServiceHolder.service = null
        }
    }

}