package net.apptronic.core.component.entity.base

/**
 * Predicate which is checks is value different
 */
open class DistinctUntilChangedUpdatePredicate<T> : UpdatePredicate<T>() {

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