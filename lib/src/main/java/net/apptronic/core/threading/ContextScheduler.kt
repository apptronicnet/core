package net.apptronic.core.threading

import net.apptronic.core.threading.Scheduler.Companion.DEFAULT

internal class ContextScheduler(
    private val parent: Scheduler? = null
) : Scheduler {

    private val fallbackWorker = synchronousWorker()
    private var defaultWorker: WorkerDefinition = DEFAULT
    private val providers = mutableMapOf<WorkerDefinition, WorkerProvider>()

    override fun setDefaultWorker(workerDefinition: WorkerDefinition) {
        this.defaultWorker = workerDefinition
    }

    override fun assignWorker(workerDefinition: WorkerDefinition, workerProvider: WorkerProvider) {
        providers[workerDefinition] = workerProvider
    }

    override fun getWorker(workerDefinition: WorkerDefinition): Worker {
        val provider = providers[workerDefinition]
        if (provider != null) {
            return provider.provideWorker()
        }
        if (parent != null) {
            return parent.getWorker(workerDefinition)
        }
        return fallbackWorker
    }

    override fun getDefaultWorker(): WorkerDefinition {
        return defaultWorker
    }

    override fun execute(workerDefinition: WorkerDefinition, action: () -> Unit) {
        getWorker(workerDefinition).execute(action)
    }

}