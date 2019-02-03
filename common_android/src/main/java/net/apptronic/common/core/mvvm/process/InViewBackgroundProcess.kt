package net.apptronic.common.core.mvvm.process

import net.apptronic.common.core.base.SerialIdGenerator
import net.apptronic.common.core.base.threading.Worker
import net.apptronic.common.core.base.threading.parallelWorker
import net.apptronic.common.core.base.threading.serialWorker
import net.apptronic.common.core.base.threading.singleWorker
import net.apptronic.common.core.component.Component
import net.apptronic.common.core.component.entity.ViewModelAbstractEntity
import net.apptronic.common.core.component.entity.functions.Predicate
import net.apptronic.common.core.component.entity.functions.variants.map
import net.apptronic.common.core.component.entity.setup

class InViewBackgroundProcess<T, R>(
    private val parent: Component,
    private val action: (T) -> R
) {

    private val idGenerator = SerialIdGenerator()

    internal var worker: Worker = serialWorker()

    private val idsInProgress = parent.valueSet<Long>()

    private val isInProgress = parent.function(idsInProgress.map { isNotEmpty() })

    private val requestEvent = parent.typedEvent<T>().setup {
        subscribe {
            doProcess(it)
        }
    }

    private val successEvent = parent.typedEvent<R>()

    private val failedEvent = parent.typedEvent<Exception>()

    fun onProgress(): Predicate<Boolean> = isInProgress

    fun onProgress(action: (Boolean) -> Unit) {
        isInProgress.subscribe(action)
    }

    fun onProgressStart(action: () -> Unit) {
        isInProgress.subscribe { if (it) action() }
    }

    fun onProgressEnd(action: () -> Unit) {
        isInProgress.subscribe { if (it.not()) action() }
    }

    fun onRequest(): ViewModelAbstractEntity<T> = requestEvent

    fun onRequest(action: (T) -> Unit) {
        requestEvent.subscribe(action)
    }

    fun onSuccess(): ViewModelAbstractEntity<R> = successEvent

    fun onSuccess(action: (R) -> Unit) {
        successEvent.subscribe(action)
    }

    fun onError(): ViewModelAbstractEntity<Exception> = failedEvent

    fun onError(action: (Exception) -> Unit) {
        failedEvent.subscribe(action)
    }

    private fun doProcess(request: T) {
        worker.run {
            val id = idGenerator.nextId()
            idsInProgress.update { add(id) }
            try {
                val result = action(request)
                successEvent.sendEvent(result)
            } catch (e: Exception) {
                failedEvent.sendEvent(e)
            } finally {
                idsInProgress.update { remove(id) }
            }
        }
    }

}

fun <P : InViewBackgroundProcess<T, R>, T, R> P.setup(setupBlock: P.() -> Unit): P {
    this.setupBlock()
    return this
}

fun <P : InViewBackgroundProcess<T, R>, T, R> P.runInParallel(): P {
    worker = parallelWorker()
    return this
}

fun <P : InViewBackgroundProcess<T, R>, T, R> P.runSingle(): P {
    worker = singleWorker()
    return this
}

fun <P : InViewBackgroundProcess<T, R>, T, R> P.runSerial(): P {
    worker = serialWorker()
    return this
}
