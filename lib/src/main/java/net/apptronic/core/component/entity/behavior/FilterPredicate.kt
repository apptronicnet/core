package net.apptronic.core.component.entity.behavior

import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.PredicateObserver
import net.apptronic.core.component.entity.Subscription
import net.apptronic.core.component.entity.subscribe

class FilterPredicate<T>(
    private val target: Predicate<T>,
    private val filterFunction: (T) -> Boolean
) : Predicate<T> {

    override fun subscribe(observer: PredicateObserver<T>): Subscription {
        return target.subscribe { value ->
            if (filterFunction(value)) {
                observer.notify(value)
            }
        }
    }

}