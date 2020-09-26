package net.apptronic.core.view.dimension

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.view.ViewProperty

interface CoreDimension : CoreLayoutDimension

class DiscreteCoreDimension internal constructor(private val value: Float) : AbstractDimensionNumber(value), CoreDimension {
    override fun toString(): String = "[$value]"
}

class PixelCoreDimension internal constructor(private val pixels: Float) : AbstractDimensionNumber(pixels), CoreDimension {
    override fun toString(): String = "$pixels pixels"

}

val Number.pixels: Number
    get() = PixelCoreDimension(this.toFloat())

fun ViewProperty<CoreDimension>.setAsCoreDimension(value: Number) {
    set(DiscreteCoreDimension(value.toFloat()))
}

fun ViewProperty<CoreDimension>.setAsCoreDimension(source: Observable<Number>) {
    set(source) {
        it.asCoreDimension()
    }
}

val CoreDimensionZero = DiscreteCoreDimension(0f)

fun Number.asCoreDimension(): CoreDimension {
    if (this is CoreDimension) {
        return this
    } else {
        return DiscreteCoreDimension(this.toFloat())
    }
}