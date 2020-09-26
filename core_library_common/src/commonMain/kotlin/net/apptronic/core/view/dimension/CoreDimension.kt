package net.apptronic.core.view.dimension

abstract class CoreDimension : Number() {

    abstract fun getFloatValue(): Float

    override fun toDouble(): Double = getFloatValue().toDouble()

    override fun toFloat(): Float = getFloatValue()

    override fun toLong(): Long = getFloatValue().toLong()

    override fun toInt(): Int = getFloatValue().toInt()

    override fun toChar(): Char = getFloatValue().toChar()

    override fun toShort(): Short = getFloatValue().toInt().toShort()

    override fun toByte(): Byte = getFloatValue().toInt().toByte()

}

fun Number.asCoreDimension(): CoreDimension {
    if (this is CoreDimension) {
        return this
    } else {
        return DiscreteDimension(this.toFloat())
    }
}

class DiscreteDimension internal constructor(private val value: Float) : CoreDimension() {
    override fun getFloatValue(): Float = value
}

class PixelDimension internal constructor(private val pixels: Float) : CoreDimension() {
    override fun getFloatValue(): Float = pixels
}

val Number.pixels: PixelDimension
    get() = PixelDimension(this.toFloat())