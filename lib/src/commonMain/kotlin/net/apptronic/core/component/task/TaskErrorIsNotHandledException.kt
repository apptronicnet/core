package net.apptronic.core.component.task

class TaskErrorIsNotHandledException internal constructor(
    cause: Exception
) : RuntimeException("Task error is not handled", cause)
