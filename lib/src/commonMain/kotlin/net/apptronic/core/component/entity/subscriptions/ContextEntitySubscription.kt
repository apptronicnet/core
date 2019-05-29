package net.apptronic.core.component.entity.subscriptions

import net.apptronic.core.base.concurrent.AtomicReference
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.EntitySubscription

internal class ContextEntitySubscription<T>(
        val observer: Observer<T>
) : EntitySubscription {

    private val isUnsubscribed = AtomicReference(false)
    private val listeners = mutableListOf<EntitySubscriptionListener>()

    override fun unsubscribe() {
        isUnsubscribed.set(true)
        listeners.forEach {
            it.onUnsubscribed(this)
        }
    }

    override fun registerListener(listener: EntitySubscriptionListener) {
        listeners.add(listener)
        if (isUnsubscribed.get()) {
            listener.onUnsubscribed(this)
        }
    }

    override fun removeListener(listener: EntitySubscriptionListener) {
        listeners.remove(listener)
    }

    override fun attachToContext(context: Context) {
        context.getLifecycle().registerSubscription(this)
    }

}