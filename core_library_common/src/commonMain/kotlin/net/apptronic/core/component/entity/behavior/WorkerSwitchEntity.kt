package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.threading.Action
import net.apptronic.core.threading.Worker
import net.apptronic.core.threading.WorkerDefinition

@Deprecated("Should use coroutines")
fun <T> Entity<T>.switchWorker(worker: Worker): Entity<T> {
    return WorkerSwitchEntity(this, worker)
}

@Deprecated("Should use coroutines")
fun <T> Entity<T>.switchWorker(
        workerDefinition: WorkerDefinition
): Entity<T> {
    val worker = context.getScheduler().getWorker(workerDefinition)
    return WorkerSwitchEntity(this, worker)
}

@Deprecated("Should use coroutines")
private class WorkerSwitchEntity<T>(
        private val target: Entity<T>,
        private val worker: Worker
) : Entity<T> {

    override val context: Context = target.context

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        return target.subscribe(context) { value ->
            worker.execute(NotifyAction(observer, value))
        }
    }

    private class NotifyAction<T>(
            val observer: Observer<T>,
            val value: T
    ) : Action {
        override fun execute() {
            observer.notify(value)
        }
    }

}