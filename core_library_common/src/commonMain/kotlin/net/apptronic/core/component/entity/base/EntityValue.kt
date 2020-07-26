package net.apptronic.core.component.entity.base

import net.apptronic.core.base.observable.subject.*
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.ValueNotSetException

/**
 * Type of [Entity] which holds value.
 */
interface EntityValue<T> : Entity<T> {

    fun getValueHolder(): ValueHolder<T>?

    /**
     * Get value
     * @throws [ValueNotSetException] is value is not set
     */
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
     * Get value or return [fallbackValue] if value is not set
     */
    fun getOr(fallbackValue: T): T {
        val valueHolder = getValueHolder()
        return if (valueHolder != null) valueHolder.value else fallbackValue
    }

    /**
     * Get value or return  instance returned by [fallbackValueProvider] if value is not set
     */
    fun getOr(fallbackValueProvider: () -> T): T {
        val valueHolder = getValueHolder()
        return if (valueHolder != null) valueHolder.value else fallbackValueProvider.invoke()
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

