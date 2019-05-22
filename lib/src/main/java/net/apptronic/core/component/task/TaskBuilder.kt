package net.apptronic.core.component.task

import net.apptronic.core.threading.WorkerDefinition

interface TaskBuilder<T> {

    fun onBeforeProcessing(
        workerDefinition: WorkerDefinition = WorkerDefinition.DEFAULT,
        action: () -> Unit
    )

    fun onBeforeTask(
        workerDefinition: WorkerDefinition = WorkerDefinition.DEFAULT,
        action: (T) -> Unit
    )

    fun onStart(workerDefinition: WorkerDefinition = WorkerDefinition.BACKGROUND_PARALLEL_INDIVIDUAL): TaskStep<T, Exception>

    fun onAfterTask(
        workerDefinition: WorkerDefinition = WorkerDefinition.DEFAULT,
        action: (T) -> Unit
    )

    fun onAfterProcessing(
        workerDefinition: WorkerDefinition = WorkerDefinition.DEFAULT,
        action: () -> Unit
    )

}