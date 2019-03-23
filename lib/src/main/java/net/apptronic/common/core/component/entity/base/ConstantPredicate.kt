package net.apptronic.common.core.component.entity.base

import net.apptronic.common.core.component.entity.Predicate
import net.apptronic.common.core.component.entity.Subscription

/**
 * Predicate with constant value
 */
class ConstantPredicate<T>(
    private val value: T
) : Predicate<T> {

    override fun subscribe(observer: (T) -> Unit): Subscription {
        observer.invoke(value)
        return StubSubscription()
    }

    private class StubSubscription : Subscription {
        override fun unsubscribe() {
            // do nothing
        }
    }

}