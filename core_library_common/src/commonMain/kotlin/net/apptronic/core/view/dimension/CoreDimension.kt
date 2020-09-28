package net.apptronic.core.view.dimension

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

fun Number.asCoreDimension(): CoreDimension {
    if (this is CoreDimension) {
        return this
    } else {
        return DiscreteCoreDimension(this.toFloat())
    }
}