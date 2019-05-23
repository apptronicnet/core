package net.apptronic.core.component.task

internal sealed class TaskExecutionResult<T, E : Exception>(val task: SchedulerTask<*>) {

    class Value<T, E : Exception>(
        task: SchedulerTask<*>, val value: T
    ) : TaskExecutionResult<T, E>(task) {

        fun <R> next(value: R): Value<R, E> {
            return Value(task, value)
        }

        override fun <TR, ER : Exception> transform(): TaskExecutionResult<TR, ER> {
            return Value(task, value as TR)
        }

        override fun getResult(): TaskResult<T> {
            return TaskResult.Success(task, value)
        }

    }

    class Error<T, E : Exception>(
        task: SchedulerTask<*>, val exception: E, val isHandled: Boolean
    ) : TaskExecutionResult<T, E>(task) {

        fun <R : Exception> next(exception: R): Error<T, R> {
            return Error(task, exception, isHandled)
        }

        override fun <TR, ER : Exception> transform(): TaskExecutionResult<TR, ER> {
            return Error(task, exception as ER, isHandled)
        }

        override fun getResult(): TaskResult<T> {
            return TaskResult.Error(task, exception, isHandled)
        }

    }

    class Interruption<T, E : Exception>(
        task: SchedulerTask<*>, val interruption: TaskInterruptedException
    ) : TaskExecutionResult<T, E>(task) {

        override fun <TR, ER : Exception> transform(): TaskExecutionResult<TR, ER> {
            return Interruption(task, interruption)
        }

        override fun getResult(): TaskResult<T> {
            return TaskResult.Interrupted(task, interruption)
        }

    }

    abstract fun <TR, ER : Exception> transform(): TaskExecutionResult<TR, ER>

    fun <TR, ER : Exception> error(cause: Exception): TaskExecutionResult<TR, ER> {
        return try {
            if (cause is TaskInterruptedException) {
                Interruption(task, cause)
            } else {
                Error(task, cause as ER, false)
            }
        } catch (e: ClassCastException) {
            Interruption(
                task,
                TaskInterruptedException(e)
            )
        }
    }

    abstract fun getResult(): TaskResult<T>

}