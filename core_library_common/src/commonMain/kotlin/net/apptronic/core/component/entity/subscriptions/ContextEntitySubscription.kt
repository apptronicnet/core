package net.apptronic.core.component.entity.subscriptions

import net.apptronic.core.base.concurrent.Volatile
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.EntitySubscription

public class ContextEntitySubscription<T>(
        val observer: Observer<T>
) : EntitySubscription {

    private val isUnsubscribed = Volatile(false)
    private val listeners = mutableListOf<EntitySubscriptionListener>()

    override fun unsubscribe() {
        isUnsubscribed.set(true)
        listeners.toTypedArray().forEach {
            it.onUnsubscribed(this)
        }
        listeners.clear()
    }

    override fun registerListener(listener: EntitySubscriptionListener) {
        if (isUnsubscribed.get()) {
            listener.onUnsubscribed(this)
        } else {
            listeners.add(listener)
        }
    }

    override fun removeListener(listener: EntitySubscriptionListener) {
        listeners.remove(listener)
    }

    override fun attachToContext(context: Context) {
        context.getLifecycle().registerSubscription(this)
    }

    override fun isUnsubscribed(): Boolean {
        return isUnsubscribed.get()
    }

    fun notify(value: T) {
        if (!isUnsubscribed()) {
            observer.notify(value)
        }
    }

}