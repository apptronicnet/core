package net.apptronic.core.base.utils

import net.apptronic.core.threading.*

class TestScheduler : Scheduler {

    private val synchronousWorker = synchronousWorker()

    override fun getDefaultWorker(): WorkerDefinition {
        return WorkerDefinition.DEFAULT
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