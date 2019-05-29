package net.apptronic.core.component.entity.subscriptions

import net.apptronic.core.component.entity.EntitySubscription

interface EntitySubscriptionListener {

    fun onUnsubscribed(subscription: EntitySubscription)

}