package net.apptronic.common.core.component.entity.entities

import net.apptronic.common.core.component.ComponentContext
import net.apptronic.common.core.component.entity.Predicate
import net.apptronic.common.core.component.entity.Subscription
import net.apptronic.common.core.component.entity.base.UpdatePredicate
import net.apptronic.common.core.component.threading.ContextWorkers

abstract class LiveModelEntity<T>(
    private val context: ComponentContext,
    protected val workingPredicate: UpdatePredicate<T>
) : Predicate<T> {

    private var workerName: String = ContextWorkers.DEFAULT

    fun setWorker(workerName: String) {
        this.workerName = workerName
    }

    override fun subscribe(observer: (T) -> Unit): Subscription {
        return workingPredicate.subscribe { value ->
            context.workers().execute(workerName) {
                observer.invoke(value)
            }
        }
    }

}