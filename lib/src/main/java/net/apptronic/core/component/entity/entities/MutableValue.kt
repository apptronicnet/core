package net.apptronic.core.component.entity.entities

import net.apptronic.core.component.context.ComponentContext
import net.apptronic.core.component.entity.base.UpdateAndStorePredicate
import net.apptronic.core.component.entity.base.ValueHolder

class MutableValue<T>(
    context: ComponentContext
) : Property<T>(
    context,
    UpdateAndStorePredicate()
) {

    internal var valueHolder: ValueHolder<T>? = null

    override fun isSet(): Boolean {
        return valueHolder != null
    }

    override fun onSetValue(value: T) {
        this.valueHolder = ValueHolder(value)
    }

    override fun onGetValue(): T {
        valueHolder?.let {
            return it.value
        } ?: throw PropertyNotSetException()
    }

    fun update(action: T.() -> Unit) {
        valueHolder?.let {
            it.value.action()
            workingPredicate.update(it.value)
        }
    }

    override fun toString(): String {
        return super.toString() + if (valueHolder == null) "/not-set" else "=$valueHolder"
    }

}

