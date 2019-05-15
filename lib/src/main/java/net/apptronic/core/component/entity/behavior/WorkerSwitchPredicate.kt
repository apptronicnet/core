package net.apptronic.core.component.entity.behavior

import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.PredicateObserver
import net.apptronic.core.component.entity.Subscription
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.threading.Worker

class WorkerSwitchPredicate<T>(
    private val target: Predicate<T>,
    private val worker: Worker
) : Predicate<T> {

    override fun subscribe(observer: PredicateObserver<T>): Subscription {
        return target.subscribe { value ->
            worker.run {
                observer.notify(value)
            }
        }
    }

}