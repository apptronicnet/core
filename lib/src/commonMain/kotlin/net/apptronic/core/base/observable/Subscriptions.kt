package net.apptronic.core.base.observable

import net.apptronic.core.platform.getPlatform

class Subscriptions<T> {

    private val sync = getPlatform().createSynchronized()
    private val subscriptions = mutableListOf<SubscriptionImpl>()

    fun createSubscription(observer: Observer<T>): Subscription {
        return sync.run {
            val subscription = SubscriptionImpl(observer)
            subscriptions.add(subscription)
            subscription
        }
    }

    fun notifyObservers(value: T) {
        val targets = sync.run {
            subscriptions.toTypedArray()
        }
        targets.forEach {
            it.observer.notify(value)
        }
    }

    private inner class SubscriptionImpl(
            val observer: Observer<T>
    ) : Subscription {

        override fun unsubscribe() {
            return sync.run {
                subscriptions.remove(this)
            }
        }

    }

}