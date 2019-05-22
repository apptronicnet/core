package net.apptronic.core.component.task

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.distinctUntilChanged
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.PublishSubject
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.UpdateEntity
import net.apptronic.core.component.entity.bindContext
import net.apptronic.core.threading.Worker
import net.apptronic.core.threading.WorkerDefinition
import net.apptronic.core.threading.execute

private class SchedulerTask<T>(
    val requestValue: T
) : Task {

    @Volatile
    private var isInterrupted = false

    override fun checkInterruption() {
        if (isInterrupted) {
            throw TaskInterruptedException("Interrupted externally")
        }
    }

    override fun interrupt() {
        isInterrupted = true
    }

    override fun isInterrupted(): Boolean {
        return isInterrupted
    }

}

fun <T> taskSchedulerBuilder(
    context: Context,
    mode: SchedulerMode,
    builder: TaskBuilder<T>.() -> Unit
): TaskScheduler<T> {
    return TaskSchedulerBuilder(context, mode, builder)
}

private class TaskSchedulerBuilder<T>(
    private val context: Context,
    private val mode: SchedulerMode,
    builder: TaskBuilder<T>.() -> Unit
) : TaskScheduler<T>,
    TaskBuilder<T> {

    private val defaultWorker = context.getScheduler().getWorker(WorkerDefinition.DEFAULT)
    private val tasksInProgress = mutableListOf<Task>()
    private val isInProgress = PublishSubject<Boolean>()

    private val actionsBeforeProcessing = mutableListOf<SchedulerActionDefinition<Unit>>()
    private val actionsBeforeRequest = mutableListOf<SchedulerActionDefinition<T>>()
    private val actionsAfterRequest = mutableListOf<SchedulerActionDefinition<T>>()
    private val actionsAfterProcessing = mutableListOf<SchedulerActionDefinition<Unit>>()

    private val onNextSource = PublishSubject<T>()
    private val sourceStep = TaskStepElement<T, Exception>(context)
    private val finalStep: TaskStepElement<*, *>

    init {
        builder.invoke(this)
        var nextStep: TaskStepElement<*, *> = sourceStep
        while (nextStep.next != null) {
            nextStep = nextStep.next!!
        }
        finalStep = nextStep
        isInProgress.distinctUntilChanged().subscribe { isProcessing ->
            if (isProcessing) {
                actionsBeforeProcessing.forEach { it.invoke(Unit) }
            } else {
                actionsAfterProcessing.forEach { it.invoke(Unit) }
            }
        }
        finalStep.onNextSubject.subscribe { result: Result<*, *> ->
            defaultWorker.execute {
                tasksInProgress.remove(result.task)
                isInProgress.update(tasksInProgress.isNotEmpty())
                actionsAfterRequest.forEach {
                    it.invoke(result.task.requestValue as T)
                }
                next()
            }
        }
    }

    private val listOfRequests = mutableListOf<SchedulerTask<T>>()

    override fun update(value: T) {
        execute(value)
    }

    override fun getContext(): Context {
        return context
    }

    override fun subscribe(observer: Observer<T>): EntitySubscription {
        return onNextSource.subscribe(observer).bindContext(context)
    }

    override fun execute(request: T): Task {
        val task = SchedulerTask(request)
        listOfRequests.add(task)
        defaultWorker.execute {
            next()
        }
        return task
    }

    private fun next() {
        val tasksToExecute = mutableListOf<SchedulerTask<T>>()
        when (mode) {
            SchedulerMode.Parallel -> {
                tasksToExecute.addAll(listOfRequests)
                listOfRequests.clear()

            }
            SchedulerMode.Single -> {
                if (tasksInProgress.isEmpty() && listOfRequests.isNotEmpty()) {
                    val task = listOfRequests.removeAt(0)
                    tasksToExecute.add(task)
                }
            }
            SchedulerMode.Debounce -> {
                if (listOfRequests.size > 1) {
                    val last = listOfRequests.get(listOfRequests.size - 1)
                    listOfRequests.clear()
                    listOfRequests.add(last)
                }
                if (tasksInProgress.isEmpty() && listOfRequests.isNotEmpty()) {
                    val task = listOfRequests.removeAt(0)
                    tasksToExecute.add(task)
                }
            }
        }
        tasksToExecute.forEach {
            executeNext(it)
        }
    }

    private fun executeNext(task: SchedulerTask<T>) {
        onNextSource.update(task.requestValue)
        tasksInProgress.add(task)
        isInProgress.update(tasksInProgress.isNotEmpty())
        actionsBeforeRequest.forEach {
            it.invoke(task.requestValue)
        }
        sourceStep.next(Result.Value(task, task.requestValue))
    }

    override fun onBeforeProcessing(workerDefinition: WorkerDefinition, action: () -> Unit) {
        val worker = context.getScheduler().getWorker(workerDefinition)
        actionsBeforeProcessing.add(
            SchedulerActionDefinition(
                worker
            ) {
                action.invoke()
            })
    }

    override fun onBeforeTask(workerDefinition: WorkerDefinition, action: (T) -> Unit) {
        val worker = context.getScheduler().getWorker(workerDefinition)
        actionsBeforeRequest.add(
            SchedulerActionDefinition(
                worker,
                action
            )
        )
    }

    override fun onStart(workerDefinition: WorkerDefinition): TaskStep<T, Exception> {
        return sourceStep.switchWorker(workerDefinition)
    }

    override fun onAfterTask(workerDefinition: WorkerDefinition, action: (T) -> Unit) {
        val worker = context.getScheduler().getWorker(workerDefinition)
        actionsAfterRequest.add(
            SchedulerActionDefinition(
                worker,
                action
            )
        )
    }

    override fun onAfterProcessing(workerDefinition: WorkerDefinition, action: () -> Unit) {
        val worker = context.getScheduler().getWorker(workerDefinition)
        actionsAfterProcessing.add(
            SchedulerActionDefinition(
                worker
            ) {
                action.invoke()
            })
    }

}

private class SchedulerActionDefinition<T>(val worker: Worker, val action: (T) -> Unit) {

    fun invoke(item: T) {
        worker.execute {
            action.invoke(item)
        }
    }

}

private sealed class Result<T, E : Exception>(val task: SchedulerTask<*>) {

    class Value<T, E : Exception>(
        task: SchedulerTask<*>, val value: T
    ) : Result<T, E>(task) {

        fun <R> next(value: R): Value<R, E> {
            return Value(task, value)
        }

        override fun <TR, ER : Exception> transform(): Result<TR, ER> {
            return Value(task, value as TR)
        }

    }

    class Error<T, E : Exception>(
        task: SchedulerTask<*>, val exception: E
    ) : Result<T, E>(task) {

        fun <R : Exception> next(exception: R): Error<T, R> {
            return Error(task, exception)
        }

        override fun <TR, ER : Exception> transform(): Result<TR, ER> {
            return Error(task, exception as ER)
        }

    }

    class Interruption<T, E : Exception>(
        task: SchedulerTask<*>, val interruption: TaskInterruptedException
    ) : Result<T, E>(task) {

        override fun <TR, ER : Exception> transform(): Result<TR, ER> {
            return Interruption(task, interruption)
        }

    }

    abstract fun <TR, ER : Exception> transform(): Result<TR, ER>

    fun <TR, ER : Exception> error(cause: Exception): Result<TR, ER> {
        return try {
            if (cause is TaskInterruptedException) {
                Interruption(task, cause)
            } else {
                Error(task, cause as ER)
            }
        } catch (e: ClassCastException) {
            Interruption(
                task,
                TaskInterruptedException(e)
            )
        }
    }

}

fun <T> newTaskStep(context: Context, request: T): TaskStep<T, Exception> {
    val task = SchedulerTask(request)
    return TaskStepElement<T, Exception>(context).apply {
        onNextSubject.update(Result.Value(task, request))
    }
}

private class TaskStepElement<T, E : Exception>(
    private val context: Context
) : TaskStep<T, E> {

    var next: TaskStepElement<*, *>? = null
    val onNextSubject = BehaviorSubject<Result<T, E>>()

    fun next(result: Result<T, E>) {
        onNextSubject.update(result)
    }

    private fun <TR, ER : Exception> nextStep(
        workerDefinition: WorkerDefinition = WorkerDefinition.SYNCHRONOUS,
        action: (Result<T, E>) -> Result<TR, ER>
    ): TaskStepElement<TR, ER> {
        val nextStep = TaskStepElement<TR, ER>(context)
        next = nextStep
        val worker = context.getScheduler().getWorker(workerDefinition)
        onNextSubject.subscribe { nextResult ->
            worker.execute {
                val next = action.invoke(nextResult)
                nextStep.next(next)
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

    override fun uiWorker(): TaskStep<T, E> {
        return switchWorker(WorkerDefinition.UI)
    }

    override fun onNext(action: Task.(T) -> Unit): TaskStep<T, E> {
        return nextStep { current ->
            try {
                if (current is Result.Value) {
                    action.invoke(current.task, current.value)
                }
                current
            } catch (e: Exception) {
                current.error<T, E>(e)
            }
        }
    }

    override fun <R> map(mapping: Task.(T) -> R): TaskStep<R, E> {
        return nextStep { current ->
            try {
                if (current is Result.Value) {
                    val next = mapping.invoke(current.task, current.value)
                    current.next(next)
                } else {
                    current.transform<R, E>()
                }
            } catch (e: Exception) {
                current.error<R, E>(e)
            }
        }
    }

    override fun onError(action: Task.(E) -> Unit): TaskStep<T, E> {
        return nextStep { current ->
            try {
                if (current is Result.Error) {
                    action.invoke(current.task, current.exception)
                }
                current
            } catch (e: Exception) {
                current.error<T, E>(e)
            }
        }
    }

    override fun <R : Exception> mapError(mapping: Task.(E) -> R): TaskStep<T, R> {
        return nextStep { current ->
            try {
                if (current is Result.Error) {
                    val next = mapping.invoke(current.task, current.exception)
                    current.next(next)
                } else {
                    current.transform<T, R>()
                }
            } catch (e: Exception) {
                current.error<T, R>(e)
            }
        }
    }

    override fun onInterrupted(action: (Task, TaskInterruptedException) -> Unit): TaskStep<T, E> {
        return nextStep { current ->
            try {
                if (current is Result.Interruption) {
                    action.invoke(current.task, current.interruption)
                }
                current
            } catch (e: Exception) {
                current.error<T, E>(e)
            }
        }
    }

    override fun sendResultTo(entity: UpdateEntity<in T>): TaskStep<T, E> {
        return nextStep { current ->
            if (current is Result.Value) {
                entity.update(current.value)
            }
            current
        }
    }

    override fun sendErrorTo(entity: UpdateEntity<in E>): TaskStep<T, E> {
        return nextStep { current ->
            if (current is Result.Error) {
                entity.update(current.exception)
            }
            current
        }
    }

    override fun doFinally(action: () -> Unit): TaskStep<T, E> {
        return nextStep { current ->
            action.invoke()
            current
        }
    }

}