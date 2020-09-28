package net.apptronic.core.view.shape

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.entities.Value
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.value
import net.apptronic.core.view.CoreView
import net.apptronic.core.view.context.CoreViewContext
import net.apptronic.core.view.dimension.CoreLayoutDimension
import net.apptronic.core.view.dimension.FitToParentDimension
import net.apptronic.core.view.properties.CoreColor

abstract class CoreShapeView(context: CoreViewContext) : CoreView(context) {

    override val width: Value<CoreLayoutDimension> = value(FitToParentDimension)
    override val height: Value<CoreLayoutDimension> = value(FitToParentDimension)

    var fillColor = value<CoreColor?>(null)
    var strokeColor = value<CoreColor?>(null)
    var strokeWidth = value<Number?>(null)

    fun fillColor(value: CoreColor?) {
        fillColor.set(value)
    }

    fun fillColor(value: Entity<CoreColor?>) {
        fillColor.setAs(value)
    }

    fun strokeColor(value: CoreColor?) {
        strokeColor.set(value)
    }

    fun strokeColor(value: Entity<CoreColor?>) {
        strokeColor.setAs(value)
    }

    fun strokeWidth(value: Number) {
        strokeWidth.set(value)
    }

    fun strokeWidth(value: Entity<Number>) {
        strokeWidth.setAs(value)
    }

}