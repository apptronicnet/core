package net.apptronic.core.view.dimension

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.entities.Value
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.entity.switchContext

interface CoreDimension : CoreLayoutDimension

class DiscreteCoreDimension internal constructor(private val value: Float) : AbstractDimensionNumber(value), CoreDimension {
    override fun toString(): String = "[$value]"
}

class PixelCoreDimension internal constructor(private val pixels: Float) : AbstractDimensionNumber(pixels), CoreDimension {
    override fun toString(): String = "$pixels pixels"

}

val Number.pixels: Number
    get() = PixelCoreDimension(this.toFloat())

val CoreDimensionZero = DiscreteCoreDimension(0f)

private fun Number.asCoreDimension(): CoreDimension {
    if (this is CoreDimension) {
        return this
    } else {
        return DiscreteCoreDimension(this.toFloat())
    }
}

fun Value<in CoreDimension>.setDimension(number: Number) {
    set(number.asCoreDimension())
}

fun Value<in CoreDimension>.setDimension(source: Entity<Number>) {
    source.switchContext(context).map { it.asCoreDimension() }.subscribe {
        set(it)
    }
}