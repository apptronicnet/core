package net.apptronic.core.base.observable.subject

import net.apptronic.core.base.concurrent.requireNeverFrozen
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.Subscription
import net.apptronic.core.base.observable.Subscriptions

/**
 * Base subject which can receive some values and resent if to subscribers
 */
open class PublishSubject<T> : Subject<T> {

    init {
        requireNeverFrozen()
    }

    private val subscriptions = Subscriptions<T>()

    override fun update(value: T) {
        subscriptions.notifyObservers(value)
    }

    override fun subscribe(observer: Observer<T>): Subscription {
        return subscriptions.createSubscription(observer)
    }

}