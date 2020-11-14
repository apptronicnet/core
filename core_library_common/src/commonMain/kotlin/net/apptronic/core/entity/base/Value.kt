package net.apptronic.core.entity.base

/**
 * Type of [Property] which holds value and supports it's updating
 */
interface Value<T> : Property<T>, SubjectEntity<T> {

    /**
     * Set [value] and notify all observers of it
     */
    fun set(value: T)

    override fun update(value: T) {
        set(value)
    }

}

