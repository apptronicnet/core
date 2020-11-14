package net.apptronic.core.entity.base

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

}

