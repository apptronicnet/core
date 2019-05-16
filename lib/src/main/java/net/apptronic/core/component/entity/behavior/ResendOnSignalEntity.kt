package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.Subscription
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription

class ResendOnSignalEntity<T>(
    private val wrappedEntity: Entity<T>
) : Entity<T> {

    private val subscriptions = mutableListOf<SubscriptionImpl>()

    private var lastValue: ValueHolder<T>? = null

    init {
        wrappedEntity.subscribe {
            lastValue = ValueHolder(it)
        }
    }

    fun resendSignal() {
        val holder = lastValue
        if (holder != null) {
            subscriptions.forEach {
                it.observer.notify(holder.value)
            }
        }
    }

    override fun getContext(): Context {
        return wrappedEntity.getContext()
    }

    override fun subscribe(observer: Observer<T>): EntitySubscription {
        val parent = wrappedEntity.subscribe(observer)
        val result = SubscriptionImpl(observer, parent)
        subscriptions.add(result)
        return result
    }

    private inner class SubscriptionImpl(
        val observer: Observer<T>,
        val wrapped: Subscription
    ) : EntitySubscription {
        override fun unsubscribe() {
            wrapped.unsubscribe()
            subscriptions.remove(this)
        }
    }

}

fun <E : ResendOnSignalEntity<T>, T> E.signalWhen(vararg entities: Entity<*>): E {
    entities.forEach {
        it.subscribe {
            resendSignal()
        }
    }
    return this
}