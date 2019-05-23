package net.apptronic.core.component.task

import net.apptronic.core.component.entity.UpdateEntity

interface TaskScheduler<T> : UpdateEntity<T> {

    fun execute(request: T): Task

}