package net.apptronic.common.android.ui.viewmodel.entity

import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

open class ViewModelValue<T>(lifecycleHolder: LifecycleHolder) : ViewModelProperty<T>(
    lifecycleHolder
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

