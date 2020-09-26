package net.apptronic.core.view.dimension

abstract class AbstractDimensionNumber(private val value: Float) : Number() {

    override fun toByte(): Byte = value.toInt().toByte()

    override fun toChar(): Char = value.toInt().toChar()

    override fun toDouble(): Double = value.toDouble()

    override fun toFloat(): Float = value

    override fun toInt(): Int = value.toInt()

    override fun toLong(): Long = value.toLong()

    override fun toShort(): Short = value.toInt().toShort()

}