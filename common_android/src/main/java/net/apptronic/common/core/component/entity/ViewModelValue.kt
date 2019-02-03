package net.apptronic.common.core.component.entity

import net.apptronic.common.core.component.lifecycle.Lifecycle

class ViewModelValue<T>(lifecycle: Lifecycle) : ViewModelProperty<T>(
    Lifecycle,
    ValueEntitySubject(lifecycleHolder)
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

    override fun toString(): String {
        return super.toString() + if (valueHolder == null) "/not-set" else "=$valueHolder"
    }

}

