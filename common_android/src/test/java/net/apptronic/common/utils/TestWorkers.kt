package net.apptronic.common.utils

import net.apptronic.common.core.base.threading.Worker
import net.apptronic.common.core.base.threading.synchronousWorker
import net.apptronic.common.core.component.threading.ContextWorkers
import net.apptronic.common.core.component.threading.ContextWorkers.Companion.DEFAULT

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