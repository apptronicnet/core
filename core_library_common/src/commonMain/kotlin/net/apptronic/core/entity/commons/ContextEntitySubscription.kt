package net.apptronic.core.entity.commons

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.context.Context
import net.apptronic.core.entity.EntitySubscription
import net.apptronic.core.entity.subscriptions.EntitySubscriptionListener

internal class ContextEntitySubscription<T>(
        val observer: Observer<T>
) : EntitySubscription {

    private var isUnsubscribed = false
    private val listeners = mutableListOf<EntitySubscriptionListener>()

    override fun unsubscribe() {
        isUnsubscribed = true
        listeners.toTypedArray().forEach {
            it.onUnsubscribed(this)
        }
        listeners.clear()
    }

    override fun registerListener(listener: EntitySubscriptionListener) {
        if (isUnsubscribed) {
            listener.onUnsubscribed(this)
        } else {
            listeners.add(listener)
        }
    }

    override fun removeListener(listener: EntitySubscriptionListener) {
        listeners.remove(listener)
    }

    override fun attachToContext(context: Context) {
        context.lifecycle.registerSubscription(this)
    }

    override fun isUnsubscribed(): Boolean {
        return isUnsubscribed
    }

    fun notify(value: T) {
        if (!isUnsubscribed()) {
            observer.update(value)
        }
    }

}