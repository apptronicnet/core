package net.apptronic.core.component.entity.base

import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.PredicateObserver
import net.apptronic.core.component.entity.Subscription

/**
 * Predicate with constant value
 */
class ConstantPredicate<T>(
    private val value: T
) : Predicate<T> {

    override fun subscribe(observer: PredicateObserver<T>): Subscription {
        observer.notify(value)
        return StubSubscription()
    }

    private class StubSubscription : Subscription {
        override fun unsubscribe() {
            // do nothing
        }
    }

}