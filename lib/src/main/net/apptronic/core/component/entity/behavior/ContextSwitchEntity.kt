package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.threading.WorkerDefinition
import net.apptronic.core.threading.execute

fun <T> Entity<T>.switchContext(context: Context): Entity<T> {
    return if (getContext().getToken() != context.getToken()) {
        ContextSwitchEntity(
            this, context
        )
    } else {
        this
    }
}

private class ContextSwitchEntity<T>(
    private val target: Entity<T>,
    private val context: Context
) : Entity<T> {

    private val worker = context.getScheduler().getWorker(WorkerDefinition.DEFAULT)

    override fun getContext(): Context {
        return context
    }

    override fun subscribe(observer: Observer<T>): EntitySubscription {
        return target.subscribe(context) { value ->
            worker.execute {
                observer.notify(value)
            }
        }
    }

}