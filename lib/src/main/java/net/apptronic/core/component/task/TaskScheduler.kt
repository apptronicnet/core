package net.apptronic.core.component.task

interface TaskScheduler<T> {

    fun execute(request: T): Task

}