package net.apptronic.core.threading

import net.apptronic.core.component.context.Context
import net.apptronic.core.platform.getPlatform

fun createCoreScheduler(context: Context): Scheduler {
    val scheduler = ContextScheduler(context)
    with(scheduler) {
        assignWorker(
                WorkerDefinition.DEFAULT,
                getPlatform().defaultWorkerProvider()
        )
        assignWorker(
                WorkerDefinition.DEFAULT_ASYNC,
                getPlatform().defaultAsyncWorkerProvider()
        )
        assignWorker(
                WorkerDefinition.SYNCHRONOUS,
                InstanceWorkerProvider(synchronousWorker())
        )
        assignWorker(
                WorkerDefinition.BACKGROUND_SINGLE_SHARED,
                InstanceWorkerProvider(singleThreadWorker())
        )
        assignWorker(
                WorkerDefinition.BACKGROUND_SINGLE_INDIVIDUAL,
                FactoryWorkerProvider { singleThreadWorker() }
        )
        assignWorker(
                WorkerDefinition.BACKGROUND_PARALLEL_SHARED,
                InstanceWorkerProvider(parallelWorker())
        )
        assignWorker(
                WorkerDefinition.BACKGROUND_PARALLEL_INDIVIDUAL,
                FactoryWorkerProvider { parallelWorker() }
        )
        assignWorker(
                WorkerDefinition.BACKGROUND_SERIAL,
                FactoryWorkerProvider { serialWorker() }
        )
        assignWorker(
                WorkerDefinition.TIMER,
                FactoryWorkerProvider { singleThreadWorker() }
        )
    }
    return scheduler
}

fun createSubScheduler(context: Context, parent: Scheduler): Scheduler {
    return ContextScheduler(context, parent)
}


interface Scheduler {

    fun getDefaultWorker(): WorkerDefinition

    fun getProvider(workerDefinition: WorkerDefinition): WorkerProvider?

    @Deprecated("Will be removed")
    fun setDefaultWorker(workerDefinition: WorkerDefinition)

    fun assignWorker(workerDefinition: WorkerDefinition, workerProvider: WorkerProvider)

    fun getWorker(workerDefinition: WorkerDefinition): Worker

    fun execute(workerDefinition: WorkerDefinition, action: () -> Unit)

}