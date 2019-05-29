package net.apptronic.core.component.entity.subscriptions

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.threading.execute

class ContextSubscriptions<T>(
        private val context: Context
) : EntitySubscriptionListener {

    private val subscriptions = mutableListOf<ContextEntitySubscription<T>>()

    fun createSubscription(
            observer: Observer<T>
    ): EntitySubscription {
        val subscription = ContextEntitySubscription(observer)
        subscriptions.add(subscription)
        subscription.registerListener(this)
        context.getLifecycle().registerSubscription(subscription)
        return subscription
    }

    fun subscribe(
            observer: Observer<T>,
            workerSource: WorkerSource
    ): EntitySubscription {
        val workerSwitchObserver = object : Observer<T> {
            override fun notify(value: T) {
                workerSource.getWorker().execute {
                    observer.notify(value)
                }
            }
        }
        return createSubscription(workerSwitchObserver)
    }

    fun subscribe(
            observer: Observer<T>,
            source: Observable<T>
    ): EntitySubscription {
        val entitySubscription = createSubscription(observer)
        val genericSubscription = source.subscribe(observer)
        entitySubscription.registerListener(object : EntitySubscriptionListener {
            override fun onUnsubscribed(subscription: EntitySubscription) {
                genericSubscription.unsubscribe()
            }
        })
        return entitySubscription
    }

    fun subscribe(
            observer: Observer<T>,
            source: Observable<T>,
            workerSource: WorkerSource
    ): EntitySubscription {
        val workerSwitchObserver = object : Observer<T> {
            override fun notify(value: T) {
                workerSource.getWorker().execute {
                    observer.notify(value)
                }
            }
        }
        return subscribe(workerSwitchObserver, source)
    }

    override fun onUnsubscribed(subscription: EntitySubscription) {
        subscriptions.remove(subscription)
    }

    fun notifyObservers(value: T) {
        subscriptions.toTypedArray().forEach {
            it.observer.notify(value)
        }
    }

}