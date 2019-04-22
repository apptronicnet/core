package net.apptronic.core.component.process

import net.apptronic.core.base.SerialIdGenerator
import net.apptronic.core.component.Component
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.functions.variants.map
import net.apptronic.core.component.entity.setup
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.threading.ContextWorkers

class BackgroundProcess<T, R>(
    private val parent: Component,
    private val action: BackgroundAction<T, R>,
    private val workerName: String = ContextWorkers.PARALLEL_BACKGROUND
) {

    private val idGenerator = SerialIdGenerator()

    private val idsInProgress = parent.valueSet<Long>()

    private val isInProgress = parent.function(idsInProgress.map { it.isNotEmpty() })

    private val requestEvent = parent.typedEvent<T>().setup {
        subscribe {
            doProcess(it)
        }
    }

    private val successEvent = parent.typedEvent<R>()

    private val failedEvent = parent.typedEvent<Exception>()

    fun onProgress(): Predicate<Boolean> = isInProgress

    fun onProgress(action: (Boolean) -> Unit) {
        onProgress().subscribe(action)
    }

    fun onProgressStart(action: () -> Unit) {
        onProgress().subscribe { if (it) action() }
    }

    fun onProgressEnd(action: () -> Unit) {
        onProgress().subscribe { if (it.not()) action() }
    }

    fun onRequest(): Predicate<T> = requestEvent

    fun onRequest(action: (T) -> Unit) {
        onRequest().subscribe(action)
    }

    fun onSuccess(): Predicate<R> = successEvent

    fun onSuccess(action: (R) -> Unit) {
        onSuccess().subscribe(action)
    }

    fun onError(): Predicate<Exception> = failedEvent

    fun onError(action: (Exception) -> Unit) {
        onError().subscribe(action)
    }

    private fun doProcess(request: T) {
        parent.getWorkers().getWorker(workerName).run {
            val id = idGenerator.nextId()
            idsInProgress.update { add(id) }
            try {
                val result = action.execute(request)
                successEvent.sendEvent(result)
            } catch (e: Exception) {
                failedEvent.sendEvent(e)
            } finally {
                idsInProgress.update { remove(id) }
            }
        }
    }

}

fun <P : BackgroundProcess<T, R>, T, R> P.setup(setupBlock: P.() -> Unit): P {
    this.setupBlock()
    return this
}