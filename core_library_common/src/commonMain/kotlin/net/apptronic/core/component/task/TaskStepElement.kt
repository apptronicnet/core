package net.apptronic.core.component.task

import net.apptronic.core.base.observable.subject.PublishSubject
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.UpdateEntity
import net.apptronic.core.threading.WorkerDefinition
import net.apptronic.core.threading.execute

internal class TaskStepElement<T, E : Exception>(
        private val context: Context
) : TaskStep<T, E> {

    private var next: TaskStepElement<*, *>? = null
    private val onNextSubject = PublishSubject<TaskExecutionResult<T, E>>()

    fun next(result: TaskExecutionResult<T, E>) {
        onNextSubject.update(result)
    }

    fun observeChainResult(observer: (TaskResult<*>) -> Unit) {
        findEnd().onNextSubject.subscribe {
            observer.invoke(it.getResult())
        }
    }

    private fun findEnd(): TaskStepElement<*, *> {
        var item: TaskStepElement<*, *> = this
        do {
            val nextItem = item.next
            if (nextItem != null) {
                item = nextItem
            }
        } while (nextItem != null)
        return item
    }

    private fun <TR, ER : Exception> nextStep(
            workerDefinition: WorkerDefinition = WorkerDefinition.SYNCHRONOUS,
            force: Boolean = false,
            action: (TaskExecutionResult<T, E>) -> TaskExecutionResult<TR, ER>
    ): TaskStepElement<TR, ER> {
        if (next != null) {
            throw IllegalStateException("Task should have only single chain")
        }
        val nextStep = TaskStepElement<TR, ER>(context)
        next = nextStep
        val worker = context.getScheduler().getWorker(workerDefinition)
        onNextSubject.subscribe { currentResult ->
            worker.execute {
                val handlingResult = when {
                    currentResult is TaskExecutionResult.Interruption -> currentResult
                    currentResult.task.isInterrupted() -> TaskExecutionResult.Interruption(
                            currentResult.task, TaskInterruptedException("Interrupted externally")
                    )
                    else -> currentResult
                }
                if (!force && handlingResult is TaskExecutionResult.Interruption) {
                    nextStep.next(handlingResult.transform())
                } else {
                    val nextResult = action.invoke(handlingResult)
                    nextStep.next(nextResult)
                }
            }
        }
        return nextStep
    }

    override fun switchWorker(workerDefinition: WorkerDefinition): TaskStep<T, E> {
        return nextStep(workerDefinition) { current ->
            current
        }
    }

    override fun defaultWorker(): TaskStep<T, E> {
        return switchWorker(WorkerDefinition.DEFAULT)
    }

    override fun onNext(action: (T) -> Unit): TaskStep<T, E> {
        return nextStep { current ->
            try {
                if (current is TaskExecutionResult.Value) {
                    action.invoke(current.value)
                }
                current
            } catch (e: Exception) {
                current.error<T, E>(e)
            }
        }
    }

    override fun <R> map(mapping: (T) -> R): TaskStep<R, E> {
        return nextStep { current ->
            try {
                if (current is TaskExecutionResult.Value) {
                    val next = mapping.invoke(current.value)
                    current.next(next)
                } else {
                    current.transform<R, E>()
                }
            } catch (e: Exception) {
                current.error<R, E>(e)
            }
        }
    }

    override fun onError(action: (E) -> Unit): TaskStep<T, E> {
        return nextStep { current ->
            try {
                if (current is TaskExecutionResult.Error) {
                    action.invoke(current.exception)
                    TaskExecutionResult.Error(current.task, current.exception, true)
                } else {
                    current
                }
            } catch (e: Exception) {
                current.error<T, E>(e)
            }
        }
    }

    override fun <R : Exception> mapError(mapping: (E) -> R): TaskStep<T, R> {
        return nextStep { current ->
            try {
                if (current is TaskExecutionResult.Error) {
                    val next = mapping.invoke(current.exception)
                    current.next(next)
                } else {
                    current.transform<T, R>()
                }
            } catch (e: Exception) {
                current.error<T, R>(e)
            }
        }
    }

    override fun onInterrupted(action: (TaskInterruptedException) -> Unit): TaskStep<T, E> {
        return nextStep(force = true) { current ->
            try {
                if (current is TaskExecutionResult.Interruption) {
                    action.invoke(current.interruption)
                }
                current
            } catch (e: Exception) {
                current.error<T, E>(e)
            }
        }
    }

    override fun sendResultTo(entity: UpdateEntity<in T>): TaskStep<T, E> {
        val worker = context.getScheduler().getWorker(WorkerDefinition.DEFAULT)
        return onNext {
            worker.execute {
                entity.update(it)
            }
        }
    }

    override fun sendErrorTo(entity: UpdateEntity<in E>): TaskStep<T, E> {
        val worker = context.getScheduler().getWorker(WorkerDefinition.DEFAULT)
        return onError {
            worker.execute {
                entity.update(it)
            }
        }
    }

    override fun doFinally(action: (TaskResult<T>) -> Unit): TaskStep<T, E> {
        return nextStep(force = true) { current ->
            try {
                action.invoke(current.getResult())
                current
            } catch (e: Exception) {
                current.error<T, E>(e)
            }
        }
    }

}