package net.apptronic.core.component.entity.behavior

import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.Subscription

class FilterPredicate<T>(
    private val target: Predicate<T>,
    private val filterFunction: (T) -> Boolean
) : Predicate<T> {

    override fun subscribe(observer: (T) -> Unit): Subscription {
        return target.subscribe { value ->
            if (filterFunction(value)) {
                observer.invoke(value)
            }
        }
    }

}