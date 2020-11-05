package net.apptronic.core.entity.commons

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.context.Context
import net.apptronic.core.context.lifecycle.LifecycleStage
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.EntitySubscription
import net.apptronic.core.entity.subscriptions.EntitySubscriptionListener

fun <T> Entity<T>.performEntitySubscription(
        targetContext: Context,
        source: Observable<T>,
        target: Observer<T>
): EntitySubscription {
    return performEntitySubscription(context, targetContext, source, target)
}

private fun <T> performEntitySubscription(
        definitionContext: Context, targetContext: Context,
        source: Observable<T>, target: Observer<T>
): EntitySubscription {
    val stages: List<LifecycleStage> = if (targetContext == definitionContext) {
        listOf(
                definitionContext.lifecycle.let { it.getActiveStage() ?: it.rootStage }
        )
    } else {
        listOf(
                definitionContext.lifecycle.rootStage,
                targetContext.lifecycle.let { it.getActiveStage() ?: it.rootStage }
        )
    }
    val allEntered = stages.all { it.isEntered() }
    return if (allEntered) {
        val entitySubscription = ContextEntitySubscription(target)
        stages.forEach {
            it.registerSubscription(entitySubscription)
        }
        val genericSubscription = source.subscribe(target)
        entitySubscription.registerListener(object : EntitySubscriptionListener {
            override fun onUnsubscribed(subscription: EntitySubscription) {
                genericSubscription.unsubscribe()
            }
        })
        entitySubscription
    } else {
        ContextEntitySubscription<T>(target).apply {
            unsubscribe()
        }
    }
}