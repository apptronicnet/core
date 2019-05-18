package net.apptronic.core.component.entity

import net.apptronic.core.base.observable.subject.ValueHolder

/**
 * Type of [Entity] which holds value.
 */
interface EntityValue<T> : Entity<T> {

    fun getValueHolder(): ValueHolder<T>?

    /**
     * Get value
     * @throws [ValueNotSetException] is value is not set
     */
    @Throws(ValueNotSetException::class)
    fun get(): T {
        return getValueHolder().get()
    }

    /**
     * Get value or return null if value is not set
     */
    fun getOrNull(): T? {
        return getValueHolder().getOrNull()
    }

    /**
     * Check is value is set
     */
    fun isSet(): Boolean {
        return getValueHolder().isSet()
    }

    /**
     * Do [action] with value if it is set
     * @return true if action performed, false if value is not set
     */
    fun doIfSet(action: (T) -> Unit): Boolean {
        return getValueHolder().doIfSet(action)
    }

}

