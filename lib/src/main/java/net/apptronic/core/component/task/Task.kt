package net.apptronic.core.component.task

interface Task {

    fun isInterrupted(): Boolean

    @Throws(TaskInterruptedException::class)
    fun checkInterruption()

    fun interrupt()

}