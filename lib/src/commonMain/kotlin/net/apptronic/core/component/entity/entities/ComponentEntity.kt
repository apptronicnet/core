package net.apptronic.core.component.entity.entities

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.bindContext
import net.apptronic.core.threading.Worker
import net.apptronic.core.threading.WorkerDefinition
import net.apptronic.core.threading.execute

abstract class ComponentEntity<T>(
    private val context: Context
) : Entity<T> {

    private var worker: Worker = context.getScheduler().getWorker(WorkerDefinition.DEFAULT)

    fun setWorker(workerDefinition: WorkerDefinition) {
        worker = context.getScheduler().getWorker(workerDefinition)
    }

    override fun getContext(): Context {
        return context
    }

    protected abstract fun getObservable(): Observable<T>

    override fun subscribe(observer: Observer<T>): EntitySubscription {
        val subscription = getObservable().subscribe { value ->
            worker.execute {
                observer.notify(value)
            }
        }
        context.getLifecycle().onExitFromActiveStage {
            subscription.unsubscribe()
        }
        return subscription.bindContext(context)
    }

}