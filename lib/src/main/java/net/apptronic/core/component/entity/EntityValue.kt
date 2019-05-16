package net.apptronic.core.component.entity

import net.apptronic.core.base.observable.Observable

/**
 * Entity is [Observable] which is bound to context
 */
interface EntityValue<T> : Entity<T> {

    @Throws(ValueNotSetException::class)
    fun get(): T

    fun getOrNull(): T?

}

