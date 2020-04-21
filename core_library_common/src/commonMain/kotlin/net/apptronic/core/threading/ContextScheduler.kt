package net.apptronic.core.threading

import net.apptronic.core.component.context.Context

@Deprecated("Should use coroutines")
class ContextScheduler(
        private val context: Context,
        private val parent: Scheduler? = null
) : Scheduler {

    private val fallbackWorker = synchronousWorker()
    private var defaultWorker: WorkerDefinition = WorkerDefinition.DEFAULT
    private val providers = mutableMapOf<WorkerDefinition, WorkerProvider>()

    override fun getProvider(workerDefinition: WorkerDefinition): WorkerProvider? {
        return providers.get(workerDefinition) ?: parent?.getProvider(workerDefinition)
    }

    override fun setDefaultWorker(workerDefinition: WorkerDefinition) {
        this.defaultWorker = workerDefinition
    }

    override fun assignWorker(workerDefinition: WorkerDefinition, workerProvider: WorkerProvider) {
        providers[workerDefinition] = workerProvider
    }

    override fun getWorker(workerDefinition: WorkerDefinition): Worker {
        val provider = getProvider(workerDefinition)
        if (provider != null) {
            return provider.provideWorker(context)
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