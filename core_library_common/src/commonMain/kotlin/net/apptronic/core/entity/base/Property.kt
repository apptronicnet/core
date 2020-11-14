package net.apptronic.core.entity.base

import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.ValueNotSetException

/**
 * Type of [Entity] which holds value.
 */
interface Property<T> : Entity<T> {

    /**
     * Get [ValueHolder] of this property. Returns null if values is not set.
     */
    fun getValueHolder(): ValueHolder<T>?

    /**
     * Get value
     * @throws [ValueNotSetException] is value is not set
     */
    fun get(): T

    /**
     * Get value or return null if value is not set
     */
    fun getOrNull(): T?

    /**
     * Get value or return [fallbackValue] if value is not set
     */
    fun getOr(fallbackValue: T): T

    /**
     * Get value or return  instance returned by [fallbackValueProvider] if value is not set
     */
    fun getOr(fallbackValueProvider: () -> T): T

    /**
     * Check is value is set
     */
    fun isSet(): Boolean

    /**
     * Do [action] with value if it is set
     * @return true if action performed, false if value is not set
     */
    fun doIfSet(action: (T) -> Unit): Boolean

}

