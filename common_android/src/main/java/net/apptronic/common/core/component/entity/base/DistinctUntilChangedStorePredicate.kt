package net.apptronic.common.core.component.entity.base

/**
 * Predicate which is stores last value and check is value different
 */
open class DistinctUntilChangedStorePredicate<T> : UpdateAndStorePredicate<T>() {

    @Volatile
    private var oldValue: ValueHolder<T>? = null

    override fun update(value: T) {
        val oldValue = this.oldValue
        if (oldValue == null || oldValue.value != value) {
            this.oldValue = ValueHolder(value)
            super.update(value)
        }
    }

}