package net.apptronic.core.component.entity.entities

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.ValueNotSetException
import net.apptronic.core.component.entity.base.UpdateEntity

abstract class AbstractValue<T>(context: Context) : Property<T>(context), UpdateEntity<T> {

    fun set(value: T) {
        subject.update(value)
    }

    final override fun update(value: T) {
        set(value)
    }

    /**
     * Set current value from given [source]
     * @return true if value set, false if no value set inside [source]
     */
    fun setFrom(source: Property<T>): Boolean {
        return try {
            set(source.get())
            true
        } catch (e: ValueNotSetException) {
            false
        }
    }

}