package net.apptronic.core.base

/**
 * Base class for subscription holders
 */
interface SubscriptionHolder {

    /**
     * Clear subscription
     */
    fun unsubscribe()

}


fun SubscriptionHolder.addTo(subscriptionHolders: SubscriptionHolders) {
    subscriptionHolders.add(this)
}