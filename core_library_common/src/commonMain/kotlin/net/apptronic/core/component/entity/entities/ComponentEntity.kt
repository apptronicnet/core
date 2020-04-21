package net.apptronic.core.component.entity.entities

import net.apptronic.core.base.concurrent.requireNeverFrozen
import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscriptions.ContextSubscriptionFactory
import net.apptronic.core.component.entity.subscriptions.WorkerSource
import net.apptronic.core.threading.Worker
import net.apptronic.core.threading.WorkerDefinition

abstract class ComponentEntity<T>(
        override val context: Context
) : Entity<T>, WorkerSource {

    init {
        requireNeverFrozen()
    }

    private var worker: Worker = context.getScheduler().getWorker(WorkerDefinition.DEFAULT)
    private val subscriptionFactory = ContextSubscriptionFactory<T>(context)

    fun setWorker(workerDefinition: WorkerDefinition) {
        worker = context.getScheduler().getWorker(workerDefinition)
    }

    protected abstract fun getObservable(): Observable<T>

    override fun getWorker(): Worker {
        return worker
    }

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        return subscriptionFactory.using(context).subscribe(observer, getObservable(), this)
    }

}