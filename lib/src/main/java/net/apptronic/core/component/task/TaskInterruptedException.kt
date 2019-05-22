package net.apptronic.core.component.task

class TaskInterruptedException : RuntimeException {

    constructor()

    constructor(cause: Exception) : super(cause)

    constructor(message: String) : super(message)

}