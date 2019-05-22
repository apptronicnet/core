package net.apptronic.core.threading

import net.apptronic.core.component.platform.PlatformHandler

fun createCoreScheduler(
    platformHandler: PlatformHandler
): Scheduler {
    val scheduler = ContextScheduler()
    with(scheduler) {
        assignWorker(WorkerDefinition.DEFAULT, platformHandler.defaultWorkerProvider())
        assignWorker(WorkerDefinition.UI, platformHandler.uiWorkerProvider())
        assignWorker(WorkerDefinition.UI_ASYNC, platformHandler.uiAsyncWorkerProvider())
        assignWorker(WorkerDefinition.SYNCHRONOUS, InstanceWorkerProvider(synchronousWorker()))
        assignWorker(
            WorkerDefinition.BACKGROUND_SINGLE_SHARED,
            InstanceWorkerProvider(singleThreadWorker(platformHandler))
        )
        assignWorker(
            WorkerDefinition.BACKGROUND_SINGLE_INDIVIDUAL,
            FactoryWorkerProvider { singleThreadWorker(platformHandler) })
        assignWorker(
            WorkerDefinition.BACKGROUND_PARALLEL_SHARED,
            InstanceWorkerProvider(parallelWorker(platformHandler))
        )
        assignWorker(
            WorkerDefinition.BACKGROUND_PARALLEL_INDIVIDUAL,
            FactoryWorkerProvider { parallelWorker(platformHandler) })
        assignWorker(
            WorkerDefinition.BACKGROUND_SERIAL,
            FactoryWorkerProvider { serialWorker(platformHandler) })
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