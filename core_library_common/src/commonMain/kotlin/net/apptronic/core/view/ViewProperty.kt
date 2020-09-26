package net.apptronic.core.view

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.Subscription
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subscribe

/**
 * Base class for some view property. Owned by [CoreView] and recycled when [CoreView] is recycled.
 */
class ViewProperty<T> internal constructor(initialValue: T) : Observable<T> {

    private var currentSubscription: Subscription? = null
    private val subscriptions = mutableListOf<Subscription>()
    private val subject = BehaviorSubject<T>()

    fun doWithValue(action: (T) -> Unit) {
        subject.getValue()?.let {
            action(it.value)
        }
    }

    init {
        subject.update(initialValue)
    }

    override fun subscribe(observer: Observer<T>): Subscription {
        return subject.subscribe(observer).also {
            subscriptions.add(it)
        }
    }

    internal fun recycle() {
        recycleCurrent()
        subscriptions.forEach {
            it.unsubscribe()
        }
        subscriptions.clear()
        currentSubscription?.unsubscribe()
        currentSubscription = null
    }

    private fun recycleCurrent() {
        subject.getValue()?.value?.let {
            if (it is Recyclable) {
                it.recycle()
            }
        }
    }

    private fun update(next: T) {
        recycleCurrent()
        subject.update(next)
    }

    fun set(value: T) {
        currentSubscription?.unsubscribe()
        currentSubscription = null
        update(value)
    }

    fun set(source: Observable<T>) {
        currentSubscription?.unsubscribe()
        currentSubscription = source.subscribe {
            update(it)
        }
    }

    fun <E> set(source: Observable<E>, function: (E) -> T) {
        currentSubscription?.unsubscribe()
        currentSubscription = source.subscribe {
            subject.update(function(it))
        }
    }

    fun get(): T {
        return subject.getValue()!!.value
    }

}

