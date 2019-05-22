package net.apptronic.core.base.utils

import net.apptronic.core.component.context.Context
import net.apptronic.core.threading.Action
import net.apptronic.core.threading.InstanceWorkerProvider
import net.apptronic.core.threading.Worker
import net.apptronic.core.threading.WorkerDefinition

fun Context.addDeferredWorker(workerDefinition: WorkerDefinition): DeferredWorker {
    val deferredWorker = DeferredWorker()
    getScheduler().assignWorker(workerDefinition, InstanceWorkerProvider(deferredWorker))
    return deferredWorker
}

class DeferredWorker : Worker {

    private val actions = mutableListOf<Action>()

    override fun execute(action: Action) {
        actions.add(action)
    }

    fun runDeferredActions() {
        val copy = actions.toTypedArray()
        actions.clear()
        copy.forEach {
            it.execute()
        }
    }

    fun runDeferredActionsRecursive() {
        while (actions.isNotEmpty()) {
            runDeferredActions()
        }
    }

}