package net.apptronic.core.component.entity.onchange

import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.UpdateEntity
import net.apptronic.core.component.entity.entities.Value
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.value

/**
 * This variant of [Entity] designed to be property, which should pass additional information to observers when it's
 * changes, but not to store this information for new observers.
 */
class OnChangeValue<T, E> internal constructor(
        context: Context
) : OnChangeProperty<T, E>(context), UpdateEntity<Next<T, E>> {

    fun set(next: T, change: E? = null) {
        update(Next(next, change))
    }

    fun update(next: T, change: E? = null) {
        update(Next(next, change))
    }

    override fun update(value: Next<T, E>) {
        this.change = value.change?.let { ValueHolder(it) }
        this.value.update(value.value)
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
