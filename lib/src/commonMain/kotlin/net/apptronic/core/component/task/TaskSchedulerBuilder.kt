package net.apptronic.core.component.task

import net.apptronic.core.base.collections.LinkedQueue
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.distinctUntilChanged
import net.apptronic.core.base.observable.subject.PublishSubject
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.bindContext
import net.apptronic.core.threading.Worker
import net.apptronic.core.threading.WorkerDefinition
import net.apptronic.core.threading.execute

internal class SchedulerTask<T>(
    val requestValue: T
) : Task {

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

    override fun getRequest(): Any {
        return requestValue as Any
    }

}

internal fun <T> taskSchedulerBuilder(
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

    init {
        builder.invoke(this)
        isInProgress.distinctUntilChanged().subscribe { isProcessing ->
            if (isProcessing) {
                actionsBeforeProcessing.forEach { it.invoke(Unit) }
            } else {
                actionsAfterProcessing.forEach { it.invoke(Unit) }
            }
        }
        sourceStep.observeChainResult { result ->
            defaultWorker.execute {
                if (result is TaskResult.Error && !result.isHandled) {
                    throw TaskErrorIsNotHandledException(result.exception)
                }
                tasksInProgress.remove(result.task)
                isInProgress.update(tasksInProgress.isNotEmpty())
                actionsAfterRequest.forEach {
                    it.invoke(result.task.getRequest() as T)
                }
                next()
            }
        }
    }

    private val listOfRequests = LinkedQueue<SchedulerTask<T>>()

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
                do {
                    listOfRequests.take()?.let { tasksToExecute.add(it) }
                } while (listOfRequests.hasItems())

            }
            SchedulerMode.Single -> {
                if (tasksInProgress.isEmpty()) {
                    listOfRequests.take()?.let { tasksToExecute.add(it) }
                }
            }
            SchedulerMode.Debounce -> {
                if (listOfRequests.size() > 1) {
                    listOfRequests.take()
                }
                if (tasksInProgress.isEmpty()) {
                    listOfRequests.take()?.let { tasksToExecute.add(it) }
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
        sourceStep.next(TaskExecutionResult.Value(task, task.requestValue))
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