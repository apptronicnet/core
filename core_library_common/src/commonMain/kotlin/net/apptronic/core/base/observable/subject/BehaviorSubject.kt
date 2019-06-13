package net.apptronic.core.base.observable.subject

import net.apptronic.core.base.concurrent.Volatile
import net.apptronic.core.base.concurrent.requireNeverFrozen
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.Subscription
import net.apptronic.core.base.observable.Subscriptions

/**
 * Entity which is store values
 */
open class BehaviorSubject<T> : Subject<T> {

    init {
        requireNeverFrozen()
    }

    private val subscriptions = Subscriptions<T>()

    private var valueHolder = Volatile<ValueHolder<T>?>(null)

    fun clear() {
        valueHolder.set(null)
    }

    override fun update(value: T) {
        valueHolder.set(ValueHolder(value))
        subscriptions.notifyObservers(value)
    }

    override fun subscribe(observer: Observer<T>): Subscription {
        valueHolder.get()?.let {
            observer.notify(it.value)
        }
        return subscriptions.createSubscription(observer)
    }

    fun getValue(): ValueHolder<T>? {
        return valueHolder.get()
    }

}