package net.apptronic.core.base.observable

class Subscriptions<T> {

    private val subscriptions = mutableListOf<SubscriptionImpl>()

    fun createSubscription(observer: Observer<T>): Subscription {
        val subscription = SubscriptionImpl(observer)
        subscriptions.add(subscription)
        return subscription
    }

    fun notifyObservers(value: T) {
        val targets = subscriptions.toTypedArray()
        targets.forEach {
            it.observer.notify(value)
        }
    }

    private inner class SubscriptionImpl(
            val observer: Observer<T>
    ) : Subscription {

        private var isUnsubscribed = false

        override fun unsubscribe() {
            isUnsubscribed = true
            subscriptions.remove(this)
        }

        override fun isUnsubscribed(): Boolean {
            return isUnsubscribed
        }

    }

}