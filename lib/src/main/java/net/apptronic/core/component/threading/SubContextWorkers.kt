package net.apptronic.core.component.threading

import net.apptronic.core.threading.Worker

class SubContextWorkers(
    private val parent: ContextWorkers
) : ContextWorkers {

    private var defaultWorker = parent.getDefaultWorker()

    private val workers = mutableMapOf<String, Worker>()

    override fun getDefaultWorker(): String {
        return defaultWorker
    }

    override fun assignWorker(workerName: String, worker: Worker) {
        workers[workerName] = worker
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