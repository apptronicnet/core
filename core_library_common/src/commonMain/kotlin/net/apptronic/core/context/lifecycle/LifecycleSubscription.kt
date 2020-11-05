package net.apptronic.core.context.lifecycle

import net.apptronic.core.base.SubscriptionHolder

class LifecycleSubscription(
    private val actionOnUnsubscribe: () -> Unit = {}
) : SubscriptionHolder {

    override fun unsubscribe() {
        actionOnUnsubscribe.invoke()
    }

}