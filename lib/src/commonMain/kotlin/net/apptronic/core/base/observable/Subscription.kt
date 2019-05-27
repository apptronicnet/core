package net.apptronic.core.base.observable

import net.apptronic.core.base.SubscriptionHolder

interface Subscription : SubscriptionHolder {

    override fun unsubscribe()

}
