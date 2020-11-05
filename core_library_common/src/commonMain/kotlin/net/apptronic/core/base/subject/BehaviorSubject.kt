package net.apptronic.core.base.subject

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.Subscription
import net.apptronic.core.base.observable.Subscriptions

/**
 * Entity which is store values
 */
open class BehaviorSubject<T> : Subject<T> {

    private val subscriptions = Subscriptions<T>()

    private var valueHolder: ValueHolder<T>? = null

    fun clear() {
        valueHolder = null
    }

    override fun update(value: T) {
        valueHolder = (ValueHolder(value))
        subscriptions.notifyObservers(value)
    }

    override fun subscribe(observer: Observer<T>): Subscription {
        valueHolder?.let {
            observer.notify(it.value)
        }
        return subscriptions.createSubscription(observer)
    }

    fun getValue(): ValueHolder<T>? {
        return valueHolder
    }

}