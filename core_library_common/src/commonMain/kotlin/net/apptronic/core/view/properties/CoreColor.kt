package net.apptronic.core.view.properties

private fun Short.bounds(): Short {
    return when {
        this < 0 -> 0
        this > 255 -> 255
        else -> this
    }
}

private fun Float.bounds(): Float {
    return when {
        this < 0f -> 0f
        this > 1f -> 1f
        else -> this
    }
}

data class CoreColor internal constructor(
        val red: Short,
        val green: Short,
        val blue: Short,
        val alpha: Float,
) {

    fun withRed(nextRed: Number): CoreColor {
        return CoreColor(nextRed.toShort().bounds(), green, blue, alpha)
    }

    fun withGreen(nextGreen: Number): CoreColor {
        return CoreColor(red, nextGreen.toShort().bounds(), blue, alpha)
    }

    fun withBlue(nextBlue: Number): CoreColor {
        return CoreColor(red, green, nextBlue.toShort().bounds(), alpha)
    }

    fun withAlpha(nextAlpha: Number): CoreColor {
        return CoreColor(red, green, blue, nextAlpha.toFloat().bounds())
    }

    fun withAlphaInt(nextAlpha: Number): CoreColor {
        return CoreColor(red, green, blue, (nextAlpha.toFloat() / 255f).bounds())
    }

    companion object {

        fun rgb(red: Number,
                green: Number,
                blue: Number
        ): CoreColor {
            return CoreColor(red.toShort().bounds(), red.toShort().bounds(), red.toShort().bounds(), 1f)
        }

        fun rgba(red: Number,
                 green: Number,
                 blue: Number,
                 alpha: Number
        ): CoreColor {
            return CoreColor(red.toShort().bounds(), red.toShort().bounds(), red.toShort().bounds(), alpha.toFloat().bounds())
        }

        fun rgbHex(rgbHex: Int, alpha: Number = 1f): CoreColor {
            val redInt = rgbHex / 0x10000 % 0x100
            val greenInt = rgbHex / 0x100 % 0x100
            val blueInt = rgbHex % 0x100
            return CoreColor(redInt.toShort(), greenInt.toShort(), blueInt.toShort(), alpha.toFloat().bounds())
        }

        fun argbHex(argbHex: Long): CoreColor {
            val alphaInt = argbHex / 0x1000000 % 0x100
            val redInt = argbHex / 0x10000 % 0x100
            val greenInt = argbHex / 0x100 % 0x100
            val blueInt = argbHex % 0x100
            return CoreColor(redInt.toShort(), greenInt.toShort(), blueInt.toShort(), (alphaInt.toFloat() / 255f).bounds())
        }

    }

}

fun coreColor(red: Short, green: Short, blue: Short, alpha: Number = 1): CoreColor {
    return CoreColor(red.bounds(), green.bounds(), blue.bounds(), alpha.toFloat().bounds())
}

val Transparent = coreColor(0, 0, 0, 0)
val White = coreColor(0, 0, 0)
val Gray = coreColor(128, 128, 128)
val Black = coreColor(0, 0, 0)
val Red = coreColor(255, 0, 0)
val Green = coreColor(0, 255, 0)
val Blue = coreColor(0, 0, 255)
val Yellow = coreColor(255, 255, 0)
val Cyan = coreColor(0, 255, 255)
val Magenta = coreColor(255, 0, 255)
