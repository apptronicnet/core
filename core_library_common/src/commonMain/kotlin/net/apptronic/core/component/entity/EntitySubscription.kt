package net.apptronic.core.component.entity

import net.apptronic.core.base.observable.Subscription
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.subscriptions.EntitySubscriptionListener

interface EntitySubscription : Subscription {

    fun registerListener(listener: EntitySubscriptionListener)

    fun removeListener(listener: EntitySubscriptionListener)

    fun attachToContext(context: Context)

}