package net.apptronic.core.threading

fun createCoreScheduler(): Scheduler {
    val scheduler = ContextScheduler()
    with(scheduler) {
        assignWorker(Scheduler.DEFAULT, InstanceWorkerProvider(defaultWorker()))
        assignWorker(Scheduler.UI, InstanceWorkerProvider(defaultWorker()))
        assignWorker(Scheduler.SYNCHRONOUS, InstanceWorkerProvider(defaultWorker()))
        assignWorker(Scheduler.BACKGROUND_SINGLE_SHARED, InstanceWorkerProvider(singleWorker()))
        assignWorker(
            Scheduler.BACKGROUND_SINGLE_INDIVIDUAL,
            FactoryWorkerProvider { singleWorker() })
        assignWorker(Scheduler.BACKGROUND_PARALLEL_SHARED, InstanceWorkerProvider(parallelWorker()))
        assignWorker(
            Scheduler.BACKGROUND_PARALLEL_INDIVIDUAL,
            FactoryWorkerProvider { parallelWorker() })
        assignWorker(Scheduler.BACKGROUND_SERIAL, FactoryWorkerProvider { serialWorker() })
    }
    return scheduler
}

fun createSubScheduler(parent: Scheduler): Scheduler {
    return ContextScheduler(parent)
}


interface Scheduler {

    companion object {
        val DEFAULT = defineWorker("DEFAULT")
        val SYNCHRONOUS = defineWorker("SYNCHRONOUS")
        val UI = defineWorker("UI")
        val BACKGROUND_SINGLE_SHARED = defineWorker("BACKGROUND_SINGLE_SHARED")
        val BACKGROUND_SINGLE_INDIVIDUAL = defineWorker("BACKGROUND_SINGLE_INDIVIDUAL")
        val BACKGROUND_PARALLEL_SHARED = defineWorker("BACKGROUND_PARALLEL_SHARED")
        val BACKGROUND_PARALLEL_INDIVIDUAL = defineWorker("BACKGROUND_PARALLEL_INDIVIDUAL")
        val BACKGROUND_SERIAL = defineWorker("SERIAL_BACKGROUND")
    }

    fun getDefaultWorker(): WorkerDefinition

    fun setDefaultWorker(workerDefinition: WorkerDefinition)

    fun assignWorker(workerDefinition: WorkerDefinition, workerProvider: WorkerProvider)

    fun getWorker(workerDefinition: WorkerDefinition): Worker

    fun execute(workerDefinition: WorkerDefinition, action: () -> Unit)

}