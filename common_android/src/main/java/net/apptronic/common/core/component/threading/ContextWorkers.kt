package net.apptronic.common.core.component.threading

import net.apptronic.common.core.base.threading.Worker

interface ContextWorkers {

    companion object {
        const val DEFAULT = "_default"
        const val UI = "_UI"
        const val SYNCHRONOUS = "_synchronous"
        const val SINGLE_BACKGROUND = "_single_background"
        const val PARALLEL_BACKGROUND = "_parallel_background"
    }

    fun getDefaultWorker(): String

    fun setDefaultWorker(workerName: String)

    fun assignWorker(workerName: String, worker: Worker)

    fun getWorker(workerName: String): Worker

    fun execute(action: () -> Unit)

    fun execute(workerName: String = DEFAULT, action: () -> Unit)

}