package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntityValue
import net.apptronic.core.component.lifecycle.Lifecycle

fun <T> Entity<T>.watch(): ValueWatcher<T> {
    return ValueWatcherImpl(this)
}

interface ValueWatcher<T> {

    /**
     * Execute some [action] for each new value in target [EntityValue]
     */
    fun forEachNewValue(action: (T) -> Unit)

    /**
     * Execute some [action] for each old value in target [EntityValue] when new value is set
     */
    fun forEachReplacedValue(action: (T) -> Unit)

    /**
     * Execute some [action] for each old value in target [EntityValue] when new value is set and for current value
     * when [Lifecycle] exits from active stage
     */
    fun forEachRecycledValue(action: (T) -> Unit)

}

private class ValueWatcherImpl<T>(
        private val source: Entity<T>
) : ValueWatcher<T> {

    override fun forEachNewValue(action: (T) -> Unit) {
        source.subscribe(NewValueActionHolder(action))
    }

    private class NewValueActionHolder<T>(val action: (T) -> Unit) : Observer<T> {
        private var valueHolder: ValueHolder<T>? = null
        override fun notify(value: T) {
            val current = valueHolder
            if (current == null || current.value !== value) {
                action.invoke(value)
                valueHolder = ValueHolder(value)

            }
        }
    }

    override fun forEachReplacedValue(action: (T) -> Unit) {
        val holder = OldValueActionHolder(action)
        source.subscribe(holder)
    }

    override fun forEachRecycledValue(action: (T) -> Unit) {
        val holder = OldValueActionHolder(action)
        source.subscribe(holder)
        source.getContext().getLifecycle().onExitFromActiveStage {
            holder.doForLast()
        }
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

}