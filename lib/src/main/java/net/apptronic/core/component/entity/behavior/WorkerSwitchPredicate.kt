package net.apptronic.core.component.entity.behavior

import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.PredicateObserver
import net.apptronic.core.component.entity.Subscription
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.threading.ContextWorkers

class WorkerSwitchPredicate<T>(
    private val target: Predicate<T>,
    private val contextWorkers: ContextWorkers,
    private val workerName: String
) : Predicate<T> {

    override fun subscribe(observer: PredicateObserver<T>): Subscription {
        return target.subscribe { value ->
            contextWorkers.execute(workerName) {
                observer.notify(value)
            }
        }
    }

}