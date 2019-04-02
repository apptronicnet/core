package net.apptronic.core.component.entity

import net.apptronic.core.base.SubscriptionHolder

interface Subscription : SubscriptionHolder {

    override fun unsubscribe()

}
