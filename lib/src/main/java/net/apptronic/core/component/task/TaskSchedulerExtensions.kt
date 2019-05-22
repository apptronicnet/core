package net.apptronic.core.component.task

import net.apptronic.core.component.context.Context
import net.apptronic.core.threading.WorkerDefinition

fun <T> Context.taskScheduler(
    mode: SchedulerMode = SchedulerMode.Parallel,
    builder: TaskBuilder<T>.() -> Unit
): TaskScheduler<T> {
    return taskSchedulerBuilder(this, mode, builder)
}

fun <T> Context.newTask(
    request: T,
    workerDefinition: WorkerDefinition = WorkerDefinition.SYNCHRONOUS,
    builder: TaskStep<T, Exception>.() -> Unit
): Task {
    val scheduler = taskScheduler<T>(SchedulerMode.Parallel) {
        builder.invoke(onStart(workerDefinition))
    }
    return scheduler.execute(request)
}