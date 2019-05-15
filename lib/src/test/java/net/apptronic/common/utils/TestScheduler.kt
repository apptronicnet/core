package net.apptronic.common.utils

import net.apptronic.core.threading.*
import net.apptronic.core.threading.Scheduler.Companion.DEFAULT

class TestScheduler : Scheduler {

    private val synchronousWorker = synchronousWorker()

    override fun getDefaultWorker(): WorkerDefinition {
        return DEFAULT
    }

    override fun setDefaultWorker(workerDefinition: WorkerDefinition) {
        // ignore
    }

    override fun assignWorker(workerDefinition: WorkerDefinition, workerProvider: WorkerProvider) {
        // ignore
    }

    override fun getWorker(workerDefinition: WorkerDefinition): Worker {
        return synchronousWorker
    }

    override fun execute(workerDefinition: WorkerDefinition, action: () -> Unit) {
        synchronousWorker.execute(action)
    }

}