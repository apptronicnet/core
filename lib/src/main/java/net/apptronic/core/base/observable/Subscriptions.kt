package net.apptronic.core.base.observable

class Subscriptions<T> {

    private val subscriptions = mutableListOf<SubscriptionImpl>()

    fun createSubscription(observer: Observer<T>): Subscription {
        val subscription = SubscriptionImpl(observer)
        subscriptions.add(subscription)
        return subscription
    }

    fun notifyObservers(value: T) {
        subscriptions.toTypedArray().forEach {
            it.observer.notify(value)
        }
    }

    private inner class SubscriptionImpl(
        val observer: Observer<T>
    ) : Subscription {

        override fun unsubscribe() {
            subscriptions.remove(this)
        }

    }

}