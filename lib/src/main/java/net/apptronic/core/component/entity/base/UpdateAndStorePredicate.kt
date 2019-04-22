package net.apptronic.core.component.entity.base

import net.apptronic.core.component.entity.PredicateObserver
import net.apptronic.core.component.entity.Subscription

/**
 * Predicate which is store values
 */
open class UpdateAndStorePredicate<T> : UpdatePredicate<T>() {

    @Volatile
    private var valueHolder: ValueHolder<T>? = null

    override fun update(value: T) {
        valueHolder = ValueHolder(value)
        super.update(value)
    }

    override fun subscribe(observer: PredicateObserver<T>): Subscription {
        valueHolder?.let {
            observer.notify(it.value)
        }
        return super.subscribe(observer)
    }

}