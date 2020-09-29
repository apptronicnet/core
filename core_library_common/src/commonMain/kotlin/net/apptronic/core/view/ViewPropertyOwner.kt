package net.apptronic.core.view

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.UpdateEntity
import net.apptronic.core.component.entity.entities.setAs

interface ViewPropertyOwner {

    val context: CoreViewContext

    fun <T> viewProperty(initialValue: T, onRecycle: ((T) -> Unit)? = null): ViewProperty<T> {
        return ViewProperty(context, initialValue, onRecycle)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> ViewProperty<T>.set(value: T) {
        (this as UpdateEntity<T>).update(value)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> ViewProperty<T>.set(entity: Entity<T>) {
        (this as UpdateEntity<T>).setAs(entity)
    }

}