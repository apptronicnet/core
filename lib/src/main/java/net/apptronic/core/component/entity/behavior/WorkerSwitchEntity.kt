package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.threading.Action
import net.apptronic.core.threading.Worker

class WorkerSwitchEntity<T>(
    private val target: Entity<T>,
    private val worker: Worker
) : Entity<T> {

    override fun getContext(): Context {
        return target.getContext()
    }

    override fun subscribe(observer: Observer<T>): EntitySubscription {
        return target.subscribe { value ->
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