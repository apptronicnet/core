package net.apptronic.core.threading

import net.apptronic.core.platform.getPlatform

fun createCoreScheduler(): Scheduler {
    val scheduler = ContextScheduler()
    with(scheduler) {
        assignWorker(WorkerDefinition.DEFAULT, getPlatform().defaultWorkerProvider())
        assignWorker(WorkerDefinition.UI, getPlatform().uiWorkerProvider())
        assignWorker(WorkerDefinition.UI_ASYNC, getPlatform().uiAsyncWorkerProvider())
        assignWorker(WorkerDefinition.SYNCHRONOUS, InstanceWorkerProvider(synchronousWorker()))
        assignWorker(
                WorkerDefinition.BACKGROUND_SINGLE_SHARED,
                InstanceWorkerProvider(singleThreadWorker())
        )
        assignWorker(
                WorkerDefinition.BACKGROUND_SINGLE_INDIVIDUAL,
                FactoryWorkerProvider { singleThreadWorker() })
        assignWorker(
                WorkerDefinition.BACKGROUND_PARALLEL_SHARED,
                InstanceWorkerProvider(parallelWorker())
        )
        assignWorker(
                WorkerDefinition.BACKGROUND_PARALLEL_INDIVIDUAL,
                FactoryWorkerProvider { parallelWorker() })
        assignWorker(
                WorkerDefinition.BACKGROUND_SERIAL,
                FactoryWorkerProvider { serialWorker() })
    }
    return scheduler
}

fun createSubScheduler(parent: Scheduler): Scheduler {
    return ContextScheduler(parent)
}


interface Scheduler {

    fun getDefaultWorker(): WorkerDefinition

    fun setDefaultWorker(workerDefinition: WorkerDefinition)

    fun assignWorker(workerDefinition: WorkerDefinition, workerProvider: WorkerProvider)

    fun getWorker(workerDefinition: WorkerDefinition): Worker

    fun execute(workerDefinition: WorkerDefinition, action: () -> Unit)

}