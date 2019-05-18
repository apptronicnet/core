package net.apptronic.core.base.observable.subject

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.Subscription

/**
 * Entity which is store values
 */
open class BehaviorSubject<T> : PublishSubject<T>() {

    @Volatile
    private var valueHolder: ValueHolder<T>? = null

    fun clear() {
        valueHolder = null
    }

    override fun update(value: T) {
        valueHolder = ValueHolder(value)
        super.update(value)
    }

    override fun subscribe(observer: Observer<T>): Subscription {
        valueHolder?.let {
            observer.notify(it.value)
        }
        return super.subscribe(observer)
    }

    fun getValue(): ValueHolder<T>? {
        return valueHolder
    }

}