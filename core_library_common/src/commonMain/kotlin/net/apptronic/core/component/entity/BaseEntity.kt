package net.apptronic.core.component.entity

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.subscriptions.EntitySubscriptionListener
import net.apptronic.core.component.lifecycle.Lifecycle

/**
 * Entity is [Observable] which is bound to [Context] and automatically works with it's [Lifecycle].
 */
abstract class BaseEntity<T> : Entity<T> {

    private val observers = mutableListOf<Observer<T>>()

    fun getObservers(): List<Observer<T>> {
        return observers
    }

    /**
     * Called when new [observer] requests to be subscribed on this [Entity]. Can be used to
     * wrap observer or perform some actions before it is subscribed.
     */
    open fun onNewObserver(targetContext: Context, observer: Observer<T>): Observer<T> {
        return observer
    }

    /**
     * Called when needed to perform subscription for [targetObserver] in [targetContext]
     */
    abstract fun onSubscribeObserver(targetContext: Context, targetObserver: Observer<T>): EntitySubscription

    /**
     * Called after [observer] is subscribed for this [Entity]
     */
    open fun onObserverSubscribed(observer: Observer<T>) {

    }

    /**
     * Called when [observer] is unsubscribed for any reason
     */
    open fun onObserverUnsubscribed(observer: Observer<T>) {

    }

    /**
     * Called when list of subscribed observers is changed (after subscribe/unsubscribe)
     */
    open fun onObserversChanged(list: List<Observer<T>>) {

    }

    final override fun subscribe(observer: Observer<T>): EntitySubscription {
        return subscribe(context, observer)
    }

    final override fun subscribe(targetContext: Context, observer: Observer<T>): EntitySubscription {
        val targetObserver = onNewObserver(targetContext, observer)
        val subscription = onSubscribeObserver(targetContext, targetObserver)
        if (!subscription.isUnsubscribed()) {
            observers.add(targetObserver)
            onObserverSubscribed(targetObserver)
            onObserversChanged(observers)
            subscription.registerListener(object : EntitySubscriptionListener {
                override fun onUnsubscribed(subscription: EntitySubscription) {
                    observers.remove(targetObserver)
                    onObserverUnsubscribed(targetObserver)
                    onObserversChanged(observers)
                }
            })
        } else {
            onObserverUnsubscribed(targetObserver)
        }
        return subscription
    }

}

