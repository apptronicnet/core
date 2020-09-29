package net.apptronic.core.view.shape

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.mapOrNull
import net.apptronic.core.component.entity.switchContext
import net.apptronic.core.view.CoreView
import net.apptronic.core.view.ViewProperty
import net.apptronic.core.view.binder.DynamicEntityReference
import net.apptronic.core.view.binder.DynamicReference
import net.apptronic.core.view.context.CoreViewContext
import net.apptronic.core.view.dimension.CoreDimension
import net.apptronic.core.view.dimension.CoreLayoutDimension
import net.apptronic.core.view.dimension.asCoreDimension
import net.apptronic.core.view.properties.CoreColor

abstract class CoreShapeView(context: CoreViewContext) : CoreView(context) {

    override val width: ViewProperty<CoreLayoutDimension> = viewProperty(FitToParent)
    override val height: ViewProperty<CoreLayoutDimension> = viewProperty(FitToParent)

    var fillColor = viewProperty<CoreColor?>(null)
    var strokeColor = viewProperty<CoreColor?>(null)
    var strokeWidth: ViewProperty<CoreDimension?> = viewProperty<CoreDimension?>(null)

    fun fillColor(value: CoreColor?) {
        fillColor.set(value)
    }

    fun fillColor(value: Entity<CoreColor?>) {
        fillColor.set(value)
    }

    fun fillColor(value: DynamicReference<CoreColor?>) {
        fillColor.set(value)
    }

    fun fillColor(value: DynamicEntityReference<CoreColor?, Entity<CoreColor?>>) {
        fillColor.set(value)
    }

    fun strokeColor(value: CoreColor?) {
        strokeColor.set(value)
    }

    fun strokeColor(value: Entity<CoreColor?>) {
        strokeColor.set(value)
    }

    fun strokeColor(value: DynamicReference<CoreColor?>) {
        strokeColor.set(value)
    }

    fun strokeColor(value: DynamicEntityReference<CoreColor?, Entity<CoreColor?>>) {
        strokeColor.set(value)
    }

    fun strokeWidth(value: Number?) {
        strokeWidth.set(value?.asCoreDimension())
    }

    fun strokeWidth(source: Entity<Number?>) {
        strokeWidth.set(source.switchContext(context).mapOrNull { it.asCoreDimension() })
    }

    fun strokeWidth(value: DynamicReference<Number?>) {
        value.subscribeWith(context) {
            strokeWidth(it)
        }
    }

    fun strokeWidth(source: DynamicEntityReference<Number?, Entity<Number?>>) {
        source.subscribeWith(context) {
            strokeWidth(it)
        }
    }

}