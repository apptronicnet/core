package net.apptronic.core.component.entity.subscriptions

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.threading.execute

class ContextSubscriptionFactory<T>(
        private val context: Context
) {

    fun using(targetContext: Context): ContextSubscriptionBuilder<T> {
        if (targetContext.getToken() == context.getToken()) {
            val shouldSubscribe = context.getLifecycle().isTerminated().not()
            return ContextSubscriptionBuilder(shouldSubscribe) { createdSubscription ->
                context.getLifecycle().getActiveStage()?.let {
                    it.registerSubscription(createdSubscription)
                } ?: run {
                    createdSubscription.unsubscribe()
                }
            }
        } else {
            val shouldSubscribe = context.getLifecycle().isTerminated().not()
                    && targetContext.getLifecycle().isTerminated().not()
            return ContextSubscriptionBuilder(shouldSubscribe) { createdSubscription ->
                context.getLifecycle().getRootStage().registerSubscription(createdSubscription)
                targetContext.getLifecycle().getActiveStage()?.let {
                    it.registerSubscription(createdSubscription)
                } ?: run {
                    createdSubscription.unsubscribe()
                }
            }
        }
    }

}

class ContextSubscriptionBuilder<T> internal constructor(
        private val shouldSubscribe: Boolean,
        private val callback: (EntitySubscription) -> Unit
) {

    fun createSubscription(
            observer: Observer<T>
    ): ContextEntitySubscription<T> {
        val subscription = ContextEntitySubscription(observer)
        callback.invoke(subscription)
        return subscription
    }

    fun subscribe(
            observer: Observer<T>,
            workerSource: WorkerSource
    ): ContextEntitySubscription<T> {
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
    ): ContextEntitySubscription<T> {
        val entitySubscription = createSubscription(observer)
        if (shouldSubscribe) {
            val genericSubscription = source.subscribe(observer)
            entitySubscription.registerListener(object : EntitySubscriptionListener {
                override fun onUnsubscribed(subscription: EntitySubscription) {
                    genericSubscription.unsubscribe()
                }
            })
        }
        return entitySubscription
    }

    fun subscribe(
            observer: Observer<T>,
            source: Observable<T>,
            workerSource: WorkerSource
    ): ContextEntitySubscription<T> {
        val workerSwitchObserver = object : Observer<T> {
            override fun notify(value: T) {
                workerSource.getWorker().execute {
                    observer.notify(value)
                }
            }
        }
        return subscribe(workerSwitchObserver, source)
    }

}