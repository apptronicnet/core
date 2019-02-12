package net.apptronic.common.core.component.entity.behavior

import net.apptronic.common.core.component.entity.Predicate
import net.apptronic.common.core.component.entity.Subscription
import net.apptronic.common.core.component.threading.ContextWorkers

class WorkerSwitchPredicate<T>(
    private val target: Predicate<T>,
    private val contextWorkers: ContextWorkers,
    private val workerName: String
) : Predicate<T> {

    override fun subscribe(observer: (T) -> Unit): Subscription {
        return target.subscribe { value ->
            contextWorkers.execute(workerName) {
                observer.invoke(value)
            }
        }
    }

}