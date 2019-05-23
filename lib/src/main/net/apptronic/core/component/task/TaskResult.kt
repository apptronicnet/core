package net.apptronic.core.component.task

sealed class TaskResult<T>(
    val task: Task
) {

    class Success<T> internal constructor(
        task: Task,
        val result: T
    ) : TaskResult<T>(task)

    class Error<T> internal constructor(
        task: Task,
        val exception: Exception,
        val isHandled: Boolean
    ) : TaskResult<T>(task)

    class Interrupted<T> internal constructor(
        task: Task,
        val interruption: TaskInterruptedException
    ) : TaskResult<T>(task)

    enum class Type {
        Success,
        Error,
        Interrupted
    }

}