package net.apptronic.core.component.task

import net.apptronic.core.component.Component
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.threading.WorkerDefinition

fun TaskScheduler<Unit>.execute() {
    execute(Unit)
}

fun Component.genericTaskScheduler(
    mode: SchedulerMode = SchedulerMode.Parallel,
    source: Entity<Unit>? = null,
    builder: TaskBuilder<Unit>.() -> Unit
): TaskScheduler<Unit> {
    return taskScheduler(mode, source, builder)
}

fun <T> Component.taskScheduler(
    mode: SchedulerMode = SchedulerMode.Parallel,
    source: Entity<T>? = null,
    builder: TaskBuilder<T>.() -> Unit
): TaskScheduler<T> {
    return taskSchedulerBuilder(this, mode, builder).also {
        if (source != null) {
            it.setAs(source)
        }
    }
}

fun <T, E : TaskScheduler<T>> E.executeWhen(vararg signals: Entity<T>): E {
    signals.forEach {
        setAs(it)
    }
    return this
}

fun <T> Component.newTask(
    request: T,
    workerDefinition: WorkerDefinition = WorkerDefinition.SYNCHRONOUS,
    builder: TaskStep<T, Exception>.() -> Unit
): Task {
    val scheduler = taskScheduler<T>(SchedulerMode.Parallel) {
        builder.invoke(onStart(workerDefinition))
    }
    return scheduler.execute(request)
}