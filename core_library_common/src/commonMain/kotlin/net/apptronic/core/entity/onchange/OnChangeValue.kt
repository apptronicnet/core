package net.apptronic.core.entity.onchange

import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.context.Context
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.SubjectEntity
import net.apptronic.core.entity.base.Value
import net.apptronic.core.entity.commons.TypedEvent
import net.apptronic.core.entity.commons.setAs
import net.apptronic.core.entity.commons.value

interface OnChangeValue<T, E> : OnChangeProperty<T, E>, Value<Next<T, E>> {

    override fun set(value: Next<T, E>) {
        update(value)
    }

    fun set(next: T, change: E? = null) {
        update(Next(next, change))
    }

    fun notify(next: T, change: E? = null) {
        update(Next(next, change))
    }

    fun getValueEntity(): Value<T> {
        val value = context.value<T>().setAs(takeValue())
        value.subscribe {
            val current = getValueHolder()
            if (current != null) {
                if (current.value !== it) {
                    set(it)
                }
            } else {
                set(it)
            }
        }
        return value
    }

}

/**
 * This variant of [Entity] designed to be property, which should pass additional information to observers when it's
 * changes, but not to store this information for new observers.
 */
class OnChangeValueImpl<T, E> internal constructor(
        context: Context
) : OnChangePropertyImpl<T, E>(context), OnChangeValue<T, E>, SubjectEntity<Next<T, E>> {

    private val updateEvent = TypedEvent<Next<T, E>>(context)

    override fun update(value: Next<T, E>) {
        this.change = value.change?.let { ValueHolder(it) }
        this.value.update(value.value)
        updateEvent.update(value)
    }

}
