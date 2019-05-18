package net.apptronic.core.component.process

import net.apptronic.core.base.SerialIdGenerator
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.Component
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.extensions.setup
import net.apptronic.core.component.entity.functions.variants.map
import net.apptronic.core.threading.Action
import net.apptronic.core.threading.WorkerDefinition

class BackgroundProcess<T, R>(
    private val parent: Component,
    private val action: BackgroundAction<T, R>,
    private val workerDefinition: WorkerDefinition = WorkerDefinition.BACKGROUND_PARALLEL_INDIVIDUAL
) {

    private val worker = parent.getScheduler().getWorker(workerDefinition)

    private val idGenerator = SerialIdGenerator()

    private val idsInProgress = parent.valueSet<Long>()

    private val isInProgress = idsInProgress.map { it.isNotEmpty() }

    private val requestEvent = parent.typedEvent<T>().setup {
        subscribe {
            doProcess(it)
        }
    }

    private val successEvent = parent.typedEvent<R>()

    private val failedEvent = parent.typedEvent<Exception>()

    fun onProgress(): Entity<Boolean> = isInProgress

    fun onProgress(action: (Boolean) -> Unit) {
        onProgress().subscribe(action)
    }

    fun onProgressStart(action: () -> Unit) {
        onProgress().subscribe { if (it) action() }
    }

    fun onProgressEnd(action: () -> Unit) {
        onProgress().subscribe { if (it.not()) action() }
    }

    fun onRequest(): Entity<T> = requestEvent

    fun onRequest(action: (T) -> Unit) {
        onRequest().subscribe(action)
    }

    fun onSuccess(): Entity<R> = successEvent

    fun onSuccess(action: (R) -> Unit) {
        onSuccess().subscribe(action)
    }

    fun onError(): Entity<Exception> = failedEvent

    fun onError(action: (Exception) -> Unit) {
        onError().subscribe(action)
    }

    private fun doProcess(request: T) {
        worker.execute(ProcessAction(request))
    }

    private inner class ProcessAction(
        private val request: T
    ) : Action {
        override fun execute() {
            val id = idGenerator.nextId()
            idsInProgress.update { it.add(id) }
            try {
                val result = action.execute(request)
                successEvent.sendEvent(result)
            } catch (e: Exception) {
                failedEvent.sendEvent(e)
            } finally {
                idsInProgress.update { it.remove(id) }
            }
        }
    }

}

fun <P : BackgroundProcess<T, R>, T, R> P.setup(setupBlock: P.() -> Unit): P {
    this.setupBlock()
    return this
}