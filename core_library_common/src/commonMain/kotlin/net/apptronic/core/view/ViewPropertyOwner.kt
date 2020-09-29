package net.apptronic.core.view

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.UpdateEntity
import net.apptronic.core.component.entity.behavior.watch
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.entity.switchContext
import net.apptronic.core.view.binder.DynamicEntityReference
import net.apptronic.core.view.binder.DynamicReference
import net.apptronic.core.view.context.CoreViewContext
import net.apptronic.core.view.dimension.CoreDimension
import net.apptronic.core.view.dimension.asCoreDimension

interface ViewPropertyOwner {

    val context: CoreViewContext

    fun <T> viewProperty(initialValue: T, onRecycle: ((T) -> Unit)? = null): ViewProperty<T> {
        return ViewPropertyImpl(context, initialValue).also {
            if (onRecycle != null) {
                it.watch().forEachRecycledValue(onRecycle)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> ViewProperty<T>.set(value: T) {
        (this as UpdateEntity<T>).update(value)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> ViewProperty<T>.set(entity: Entity<T>) {
        (this as UpdateEntity<T>).setAs(entity)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> ViewProperty<T>.set(dynamicReference: DynamicReference<T>) {
        val updateEntity = this as UpdateEntity<T>
        dynamicReference.subscribeWith(context) {
            updateEntity.update(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T, E : Entity<T>> ViewProperty<T>.set(dynamicReference: DynamicEntityReference<T, E>) {
        val updateEntity = this as UpdateEntity<T>
        dynamicReference.subscribeWith(context) {
            updateEntity.setAs(it)
        }
    }

    fun ViewProperty<in CoreDimension>.setDimension(number: Number) {
        set(number.asCoreDimension())
    }

    fun ViewProperty<in CoreDimension>.setDimension(source: Entity<Number>) {
        source.switchContext(context).map { it.asCoreDimension() }.subscribe {
            set(it)
        }
    }

    fun ViewProperty<in CoreDimension>.setDimension(number: DynamicReference<Number>) {
        number.subscribeWith(context) {
            setDimension(it)
        }
    }

    fun ViewProperty<in CoreDimension>.setDimension(source: DynamicEntityReference<Number, Entity<Number>>) {
        source.subscribeWith(context) {
            set(it.switchContext(context).map { it.asCoreDimension() })
        }
    }

}