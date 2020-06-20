package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.base.RelayEntity
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.entity.subscriptions.EntitySubscriptionListener
import net.apptronic.core.component.lifecycle.LifecycleStage

fun <T> Entity<T>.watch(): ValueWatcher<T> {
    return ValueWatcherImpl(this)
}

interface ValueWatcher<T> : Entity<T> {

    /**
     * Execute some [action] for each value in target [Entity], but ignoring case when new values is same as
     * existing value
     */
    fun forEachValue(action: (T) -> Unit): ValueWatcher<T>

    /**
     * Same as [forEachValue] but ignoring first (or currently set) value.
     */
    fun forEachNewValue(action: (T) -> Unit): ValueWatcher<T>

    /**
     * Execute some [action] for each old value in target [Entity] when new value is set
     */
    fun forEachReplacedValue(action: (T) -> Unit): ValueWatcher<T>

    /**
     * Execute some [action] for same values as [forEachReplacedValue], but in addition calls for current value
     * when entity is recycled by exiting for corresponding [LifecycleStage]
     */
    fun forEachRecycledValue(action: (T) -> Unit): ValueWatcher<T>

    /**
     * Subscribe to target [Entity] and wait until it will unsubscribe automatically by lifecycle conditions.
     */
    fun whenAutoUnsubscribed(action: () -> Unit)

}

private class ValueWatcherImpl<T>(
        source: Entity<T>
) : RelayEntity<T>(source), ValueWatcher<T> {

    override fun forEachValue(action: (T) -> Unit): ValueWatcher<T> {
        source.subscribe(ValueActionHolder(action))
        return this
    }

    private class ValueActionHolder<T>(val action: (T) -> Unit) : Observer<T> {
        private var valueHolder: ValueHolder<T>? = null
        override fun notify(value: T) {
            val current = valueHolder
            if (current == null || current.value !== value) {
                action.invoke(value)
                valueHolder = ValueHolder(value)

            }
        }
    }

    override fun forEachNewValue(action: (T) -> Unit): ValueWatcher<T> {
        source.subscribe(NewValueActionHolder(action))
        return this
    }

    private class NewValueActionHolder<T>(val action: (T) -> Unit) : Observer<T> {
        private var valueHolder: ValueHolder<T>? = null
        override fun notify(value: T) {
            val current = valueHolder
            valueHolder = ValueHolder(value)
            if (current != null && current.value !== value) {
                action.invoke(value)
            }
        }
    }

    override fun forEachReplacedValue(action: (T) -> Unit): ValueWatcher<T> {
        val holder = OldValueActionHolder(action)
        source.subscribe(holder)
        return this
    }

    override fun forEachRecycledValue(action: (T) -> Unit): ValueWatcher<T> {
        val holder = OldValueActionHolder(action)
        source.subscribe(holder)
        source.context.lifecycle.onExitFromActiveStage {
            holder.doForLast()
        }
        return this
    }

    private class OldValueActionHolder<T>(val action: (T) -> Unit) : Observer<T> {
        private var valueHolder: ValueHolder<T>? = null
        override fun notify(value: T) {
            valueHolder?.let {
                if (value !== it.value) {
                    action.invoke(it.value)
                }
            }
            valueHolder = ValueHolder(value)
        }

        fun doForLast() {
            valueHolder?.let {
                action.invoke(it.value)
            }
            valueHolder = null
        }
    }

    override fun whenAutoUnsubscribed(action: () -> Unit) {
        val subscription = source.subscribe {
            // ignore
        }
        subscription.registerListener(object : EntitySubscriptionListener {
            override fun onUnsubscribed(subscription: EntitySubscription) {
                action.invoke()
            }
        })
    }

}