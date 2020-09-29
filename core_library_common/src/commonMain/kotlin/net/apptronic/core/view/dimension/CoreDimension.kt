package net.apptronic.core.view.dimension

interface CoreDimension : CoreLayoutDimension

class DiscreteCoreDimension internal constructor(val size: Float) : AbstractDimensionNumber(size), CoreDimension {
    override fun toString(): String = "[$size]"
}

class PixelCoreDimension internal constructor(val pixels: Float) : AbstractDimensionNumber(pixels), CoreDimension {
    override fun toString(): String = "$pixels pixels"

}

val Number.pixels: AbstractDimensionNumber
    get() = PixelCoreDimension(this.toFloat())

val Number.dimension: AbstractDimensionNumber
    get() = PixelCoreDimension(this.toFloat())

val CoreDimensionZero = DiscreteCoreDimension(0f)

fun Number.asCoreDimension(): CoreDimension {
    if (this is CoreDimension) {
        return this
    } else {
        return DiscreteCoreDimension(this.toFloat())
    }
}