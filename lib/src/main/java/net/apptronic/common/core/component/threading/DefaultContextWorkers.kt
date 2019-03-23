package net.apptronic.common.core.component.threading

import net.apptronic.common.core.base.threading.*
import net.apptronic.common.core.component.threading.ContextWorkers.Companion.DEFAULT
import net.apptronic.common.core.component.threading.ContextWorkers.Companion.PARALLEL_BACKGROUND
import net.apptronic.common.core.component.threading.ContextWorkers.Companion.SINGLE_BACKGROUND
import net.apptronic.common.core.component.threading.ContextWorkers.Companion.SYNCHRONOUS
import net.apptronic.common.core.component.threading.ContextWorkers.Companion.UI

class DefaultContextWorkers : ContextWorkers {

    private val fallbackWorker = synchronousWorker()
    private var defaultWorker: String = DEFAULT
    private val workers = mutableMapOf<String, Worker>()

    init {
        assignWorker(DEFAULT, defaultWorker())
        assignWorker(UI, defaultWorker())
        assignWorker(SYNCHRONOUS, defaultWorker())
        assignWorker(SINGLE_BACKGROUND, singleWorker())
        assignWorker(PARALLEL_BACKGROUND, parallelWorker())
    }

    override fun setDefaultWorker(workerName: String) {
        this.defaultWorker = workerName
    }

    override fun assignWorker(workerName: String, worker: Worker) {
        workers[workerName] = worker
    }

    override fun getWorker(workerName: String): Worker {
        return workers[workerName] ?: fallbackWorker
    }

    override fun getDefaultWorker(): String {
        return defaultWorker
    }

    override fun execute(workerName: String, action: () -> Unit) {
        getWorker(workerName).run(action)
    }

}