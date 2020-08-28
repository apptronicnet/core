package net.apptronic.core.component.entity.flow

import net.apptronic.core.base.SubscriptionHolders
import net.apptronic.core.base.addTo
import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.Subscription
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.*
import net.apptronic.core.component.entity.entities.performEntitySubscription

class FlowEntity<T>(
        sources: List<Entity<out T>>,
        private val flow: Flow<T>
) : BaseEntity<T>() {

    private val sources = sources.toTypedArray()

    override val context: Context = collectContext(*sources.toTypedArray())

    interface Flow<T> {

        fun process(sources: List<ValueHolder<T>?>, updatedIndex: Int): ValueHolder<T>?

    }

    override fun onSubscribeObserver(targetContext: Context, targetObserver: Observer<T>): EntitySubscription {
        return performEntitySubscription(targetContext, FlowObservable(), targetObserver)
    }

    private inner class FlowObservable : Observable<T> {

        override fun subscribe(observer: Observer<T>): Subscription {
            val values = arrayOfNulls<ValueHolder<T>>(sources.size)
            val holders = SubscriptionHolders()
            sources.forEachIndexed { index, entity ->
                entity.subscribe { next ->
                    values[index] = ValueHolder((next))
                    val result = flow.process(values.toList(), index)
                    result?.let {
                        observer.notify(it.value)
                    }
                }.addTo(holders)
            }
            return SubscriptionWithHolders(holders)
        }

    }

    private class SubscriptionWithHolders(val holders: SubscriptionHolders) : Subscription {

        var unsubscribed = false

        override fun isUnsubscribed(): Boolean {
            return unsubscribed
        }

        override fun unsubscribe() {
            holders.unsubscribe()
            unsubscribed = true
        }
    }

}