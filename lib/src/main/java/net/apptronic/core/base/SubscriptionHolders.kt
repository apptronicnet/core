package net.apptronic.core.base

class SubscriptionHolders : SubscriptionHolder {

    private val innerList = mutableListOf<SubscriptionHolder>()

    fun add(subscriptionHolder: SubscriptionHolder) {
        innerList.add(subscriptionHolder)
    }

    override fun unsubscribe() {
        innerList.forEach {
            it.unsubscribe()
        }
        innerList.clear()
    }

}