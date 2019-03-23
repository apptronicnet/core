package net.apptronic.core.component.entity.base

import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.Subscription

/**
 * Base predicate which can receive some values and resent if to subscribers
 */
open class UpdatePredicate<T> : Predicate<T> {

    private val subscriptions = mutableListOf<SubscriptionImpl>()

    open fun update(value: T) {
        subscriptions.toTypedArray().forEach {
            it.observer.invoke(value)
        }
    }

    override fun subscribe(observer: (T) -> Unit): Subscription {
        val subscription = SubscriptionImpl(observer)
        subscriptions.add(subscription)
        return subscription
    }

    private inner class SubscriptionImpl(
        val observer: (T) -> Unit
    ) : Subscription {

        override fun unsubscribe() {
            subscriptions.remove(this)
        }

    }

}