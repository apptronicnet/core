package net.apptronic.core.entity

import net.apptronic.core.base.observable.Subscription
import net.apptronic.core.context.Context
import net.apptronic.core.entity.subscriptions.EntitySubscriptionListener

interface EntitySubscription : Subscription {

    fun registerListener(listener: EntitySubscriptionListener)

    fun removeListener(listener: EntitySubscriptionListener)

    fun attachToContext(context: Context)

}