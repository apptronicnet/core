package net.apptronic.core.component.entity.behavior

import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.PredicateObserver
import net.apptronic.core.component.entity.Subscription
import net.apptronic.core.component.entity.base.ValueHolder
import net.apptronic.core.component.entity.subscribe

class ResendOnSignalPredicate<T>(
    private val wrappedPredicate: Predicate<T>
) : Predicate<T> {

    private val subscriptions = mutableListOf<SubscriptionImpl>()

    private var lastValue: ValueHolder<T>? = null

    init {
        wrappedPredicate.subscribe {
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

    override fun subscribe(observer: PredicateObserver<T>): Subscription {
        val parent = wrappedPredicate.subscribe(observer)
        val result = SubscriptionImpl(observer, parent)
        subscriptions.add(result)
        return result
    }

    private inner class SubscriptionImpl(
        val observer: PredicateObserver<T>,
        val wrapped: Subscription
    ) : Subscription {
        override fun unsubscribe() {
            wrapped.unsubscribe()
            subscriptions.remove(this)
        }
    }

}

fun <E : ResendOnSignalPredicate<T>, T> E.signalWhen(vararg predicates: Predicate<*>): E {
    predicates.forEach {
        it.subscribe {
            resendSignal()
        }
    }
    return this
}