package net.apptronic.core.component.task

interface Task {

    fun isInterrupted(): Boolean

    fun checkInterruption()

    fun interrupt()

    fun getRequest(): Any

}