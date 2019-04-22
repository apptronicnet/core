package net.apptronic.core.component.entity.base

import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.PredicateObserver
import net.apptronic.core.component.entity.Subscription

/**
 * Base predicate which can receive some values and resent if to subscribers
 */
open class UpdatePredicate<T> : Predicate<T> {

    private val subscriptions = mutableListOf<SubscriptionImpl>()

    open fun update(value: T) {
        subscriptions.toTypedArray().forEach {
            it.observer.notify(value)
        }
    }

    override fun subscribe(observer: PredicateObserver<T>): Subscription {
        val subscription = SubscriptionImpl(observer)
        subscriptions.add(subscription)
        return subscription
    }

    private inner class SubscriptionImpl(
        val observer: PredicateObserver<T>
    ) : Subscription {

        override fun unsubscribe() {
            subscriptions.remove(this)
        }

    }

}