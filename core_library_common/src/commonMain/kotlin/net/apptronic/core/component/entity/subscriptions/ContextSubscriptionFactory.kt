package net.apptronic.core.component.entity.subscriptions

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription

fun <T> Entity<T>.subscriptionBuilder(target: Context): ContextSubscriptionBuilder<T> {
    return ContextSubscriptionFactory<T>(context).using(target)
}

class ContextSubscriptionFactory<T>(
        private val definitionContext: Context
) {

    fun using(targetContext: Context): ContextSubscriptionBuilder<T> {
        if (targetContext == definitionContext) {
            val shouldSubscribe = definitionContext.lifecycle.isTerminated().not()
            return ContextSubscriptionBuilder(shouldSubscribe) { createdSubscription ->
                definitionContext.lifecycle.getActiveStage()?.let {
                    it.registerSubscription(createdSubscription)
                } ?: run {
                    createdSubscription.unsubscribe()
                }
            }
        } else {
            val shouldSubscribe = definitionContext.lifecycle.isTerminated().not()
                    && targetContext.lifecycle.isTerminated().not()
            return ContextSubscriptionBuilder(shouldSubscribe) { createdSubscription ->
                definitionContext.lifecycle.rootStage.registerSubscription(createdSubscription)
                targetContext.lifecycle.getActiveStage()?.let {
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

}