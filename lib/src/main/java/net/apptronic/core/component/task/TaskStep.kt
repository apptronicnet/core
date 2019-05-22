package net.apptronic.core.component.task

import net.apptronic.core.component.entity.UpdateEntity
import net.apptronic.core.threading.WorkerDefinition

interface TaskStep<T, E : Exception> {

    fun switchWorker(workerDefinition: WorkerDefinition): TaskStep<T, E>

    fun uiWorker(): TaskStep<T, E>

    fun defaultWorker(): TaskStep<T, E>

    fun onNext(action: Task.(T) -> Unit): TaskStep<T, E>

    fun <R> map(mapping: Task.(T) -> R): TaskStep<R, E>

    fun onError(action: Task.(E) -> Unit): TaskStep<T, E>

    fun <R : Exception> mapError(mapping: Task.(E) -> R): TaskStep<T, R>

    fun onInterrupted(action: (Task, TaskInterruptedException) -> Unit): TaskStep<T, E>

    fun sendResultTo(entity: UpdateEntity<in T>): TaskStep<T, E>

    fun sendErrorTo(entity: UpdateEntity<in E>): TaskStep<T, E>

    fun doFinally(action: () -> Unit): TaskStep<T, E>

}