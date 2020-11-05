package net.apptronic.core.entity.subscriptions

import net.apptronic.core.entity.EntitySubscription

interface EntitySubscriptionListener {

    fun onUnsubscribed(subscription: EntitySubscription)

}