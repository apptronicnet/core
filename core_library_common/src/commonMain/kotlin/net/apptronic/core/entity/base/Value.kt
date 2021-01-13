package net.apptronic.core.entity.base

import net.apptronic.core.entity.ValueNotSetException

/**
 * Type of [Property] which holds value and supports it's set (or update)
 */
interface Value<T> : Property<T>, SubjectEntity<T> {

    /**
     * Set [value] and notify all observers of it
     */
    fun set(value: T)

    /**
     * Update simply sets new value
     */
    override fun update(value: T)

    /**
     * Update to new value based on current value
     */
    fun updateValue(updateCall: (T) -> T)

    /**
     * Clear stored value:
     * - current subscribers will not receive any updates
     * - new subscribers will not receive any updates until until new value set
     * - [get] will throw [ValueNotSetException] until new value set
     * - [isSet] will return false until new value set
     */
    fun clear()

}

