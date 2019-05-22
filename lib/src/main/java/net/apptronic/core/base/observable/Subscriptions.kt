package net.apptronic.core.base.observable

class Subscriptions<T> {

    private val subscriptions = mutableListOf<SubscriptionImpl>()

    fun createSubscription(observer: Observer<T>): Subscription {
        synchronized(subscriptions) {
            val subscription = SubscriptionImpl(observer)
            subscriptions.add(subscription)
            return subscription
        }
    }

    fun notifyObservers(value: T) {
        val targets = synchronized(subscriptions) {
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
            synchronized(subscriptions) {
                subscriptions.remove(this)
            }
        }

    }

}