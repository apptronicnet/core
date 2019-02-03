package net.apptronic.common.core.component.threading

import net.apptronic.common.core.base.threading.Worker

class SubContextWorkers(
    private val parent: ContextWorkers
) : ContextWorkers {

    private var defaultWorker = parent.getDefaultWorker()

    private val workers = mutableMapOf<String, Worker>()

    override fun getDefaultWorker(): String {
        return defaultWorker
    }

    override fun assignWorker(threadName: String, worker: Worker) {
        workers[threadName] = worker
    }

    override fun execute(action: () -> Unit) {
        getWorker(defaultWorker).run(action)
    }

    override fun getWorker(workerName: String): Worker {
        return workers[workerName] ?: parent.getWorker(workerName)
    }

    override fun execute(workerName: String, action: () -> Unit) {
        getWorker(workerName).run(action)
    }

    override fun setDefaultWorker(workerName: String) {
        defaultWorker = workerName
    }

}