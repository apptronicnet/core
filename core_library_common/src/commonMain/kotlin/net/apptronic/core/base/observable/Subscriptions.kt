package net.apptronic.core.base.observable

import net.apptronic.core.base.concurrent.AtomicReference
import net.apptronic.core.base.concurrent.Synchronized

class Subscriptions<T> {

    private val sync = Synchronized()
    private val subscriptions = mutableListOf<SubscriptionImpl>()

    fun createSubscription(observer: Observer<T>): Subscription {
        return sync.executeBlock {
            val subscription = SubscriptionImpl(observer)
            subscriptions.add(subscription)
            subscription
        }
    }

    fun notifyObservers(value: T) {
        val targets = sync.executeBlock {
            subscriptions.toTypedArray()
        }
        targets.forEach {
            it.observer.notify(value)
        }
    }

    private inner class SubscriptionImpl(
            val observer: Observer<T>
    ) : Subscription {

        private var isUnsubscribed = AtomicReference(false)

        override fun unsubscribe() {
            isUnsubscribed.set(true)
            return sync.executeBlock {
                subscriptions.remove(this)
            }
        }

        override fun isUnsubscribed(): Boolean {
            return isUnsubscribed.get()
        }

    }

}