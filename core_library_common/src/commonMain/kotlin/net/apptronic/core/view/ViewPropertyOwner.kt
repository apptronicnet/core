package net.apptronic.core.view

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.entity.Entity

@UnderDevelopment
interface ViewPropertyOwner {

    val context: CoreViewContext

    fun <T> viewProperty(initialValue: T, onRecycle: ((T) -> Unit)? = null): ViewProperty<T>

    @Suppress("UNCHECKED_CAST")
    fun <T> ViewProperty<T>.set(value: T)

    @Suppress("UNCHECKED_CAST")
    fun <T> ViewProperty<T>.set(source: Entity<T>)

}