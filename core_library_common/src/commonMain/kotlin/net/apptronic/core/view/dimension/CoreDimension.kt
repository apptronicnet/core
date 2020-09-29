package net.apptronic.core.view.dimension

abstract class CoreDimension(private val rawNumber: Float) : Number(), CoreLayoutDimension {

    override fun toByte(): Byte = rawNumber.toInt().toByte()

    override fun toChar(): Char = rawNumber.toInt().toChar()

    override fun toDouble(): Double = rawNumber.toDouble()

    override fun toFloat(): Float = rawNumber

    override fun toInt(): Int = rawNumber.toInt()

    override fun toLong(): Long = rawNumber.toLong()

    override fun toShort(): Short = rawNumber.toInt().toShort()


}

class DiscreteCoreDimension(val size: Float) : CoreDimension(size) {
    override fun toString(): String = "[$size]"
}

class PixelCoreDimension(val pixels: Float) : CoreDimension(pixels) {
    override fun toString(): String = "$pixels pixels"

}

fun Number.asCoreDimension(): CoreDimension {
    if (this is CoreDimension) {
        return this
    } else {
        return DiscreteCoreDimension(this.toFloat())
    }
}