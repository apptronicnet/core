package net.apptronic.common.utils

import net.apptronic.core.component.threading.ContextWorkers
import net.apptronic.core.component.threading.ContextWorkers.Companion.DEFAULT
import net.apptronic.core.threading.Worker
import net.apptronic.core.threading.synchronousWorker

class TestWorkers : ContextWorkers {

    private val synchronousWorker = synchronousWorker()

    init {
    }

    override fun setDefaultWorker(workerName: String) {
    }

    override fun assignWorker(workerName: String, worker: Worker) {
    }

    override fun getWorker(workerName: String): Worker {
        return synchronousWorker
    }

    override fun getDefaultWorker(): String {
        return DEFAULT
    }

    override fun execute(workerName: String, action: () -> Unit) {
        getWorker(workerName).run(action)
    }

}