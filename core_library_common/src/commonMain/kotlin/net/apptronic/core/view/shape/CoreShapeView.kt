package net.apptronic.core.view.shape

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.view.CoreView
import net.apptronic.core.view.ViewProperty
import net.apptronic.core.view.binder.DynamicEntityReference
import net.apptronic.core.view.binder.DynamicReference
import net.apptronic.core.view.context.CoreViewContext
import net.apptronic.core.view.dimension.CoreLayoutDimension
import net.apptronic.core.view.dimension.FitToParentDimension
import net.apptronic.core.view.properties.CoreColor

abstract class CoreShapeView(context: CoreViewContext) : CoreView(context) {

    override val width: ViewProperty<CoreLayoutDimension> = viewProperty(FitToParentDimension)
    override val height: ViewProperty<CoreLayoutDimension> = viewProperty(FitToParentDimension)

    var fillColor = viewProperty<CoreColor?>(null)
    var strokeColor = viewProperty<CoreColor?>(null)
    var strokeWidth: ViewProperty<Number?> = viewProperty<Number?>(null)

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
        strokeWidth.set(value)
    }

    fun strokeWidth(value: Entity<Number?>) {
        strokeWidth.set(value)
    }

    fun strokeWidth(value: DynamicReference<Number?>) {
        strokeWidth.set(value)
    }

    fun strokeWidth(value: DynamicEntityReference<Number?, Entity<Number?>>) {
        strokeWidth.set(value)
    }

}