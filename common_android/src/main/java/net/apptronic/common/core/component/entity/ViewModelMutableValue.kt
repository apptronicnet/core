package net.apptronic.common.core.component.entity

class ViewModelMutableValue<T>(
    lifecycleHolder: LifecycleHolder
) : ViewModelProperty<T>(
    lifecycleHolder,
    MutableValueSubject(lifecycleHolder)
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

    fun update(action: T.() -> Unit) {
        lifecycleHolder.provideThreadExecutor().execute {
            valueHolder?.value?.action()
        }
    }

}

