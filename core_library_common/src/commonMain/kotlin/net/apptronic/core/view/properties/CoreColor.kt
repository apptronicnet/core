package net.apptronic.core.view.properties

import net.apptronic.core.UnderDevelopment

@UnderDevelopment
private fun Int.bounds(): Int {
    return when {
        this < 0 -> 0
        this > 255 -> 255
        else -> this
    }
}

@UnderDevelopment
private fun Float.bounds(): Float {
    return when {
        this < 0f -> 0f
        this > 1f -> 1f
        else -> this
    }
}

@UnderDevelopment
data class CoreColor internal constructor(
        val red: Int,
        val green: Int,
        val blue: Int,
        val alpha: Float,
) {

    val alphaInt: Int
        get() = (alpha * 255f).toInt()

    fun withRed(nextRed: Number): CoreColor {
        return CoreColor(nextRed.toInt().bounds(), green, blue, alpha)
    }

    fun withGreen(nextGreen: Number): CoreColor {
        return CoreColor(red, nextGreen.toInt().bounds(), blue, alpha)
    }

    fun withBlue(nextBlue: Number): CoreColor {
        return CoreColor(red, green, nextBlue.toInt().bounds(), alpha)
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
            return CoreColor(red.toInt().bounds(), red.toInt().bounds(), red.toInt().bounds(), 1f)
        }

        fun rgba(red: Number,
                 green: Number,
                 blue: Number,
                 alpha: Number
        ): CoreColor {
            return CoreColor(red.toInt().bounds(), red.toInt().bounds(), red.toInt().bounds(), alpha.toFloat().bounds())
        }

        fun rgbHex(rgbHex: Int, alpha: Number = 1f): CoreColor {
            val redInt = rgbHex / 0x10000 % 0x100
            val greenInt = rgbHex / 0x100 % 0x100
            val blueInt = rgbHex % 0x100
            return CoreColor(redInt.toInt(), greenInt.toInt(), blueInt.toInt(), alpha.toFloat().bounds())
        }

        fun argbHex(argbHex: Long): CoreColor {
            val alphaInt = argbHex / 0x1000000 % 0x100
            val redInt = argbHex / 0x10000 % 0x100
            val greenInt = argbHex / 0x100 % 0x100
            val blueInt = argbHex % 0x100
            return CoreColor(redInt.toInt(), greenInt.toInt(), blueInt.toInt(), (alphaInt.toFloat() / 255f).bounds())
        }

    }

}

@UnderDevelopment
fun coreColor(red: Number, green: Number, blue: Number, alpha: Number = 1): CoreColor {
    return CoreColor(red.toInt().bounds(), green.toInt().bounds(), blue.toInt().bounds(), alpha.toFloat().bounds())
}

@UnderDevelopment
val ColorTransparent = coreColor(0, 0, 0, 0)

@UnderDevelopment
val ColorWhite = coreColor(0, 0, 0)

@UnderDevelopment
val ColorGray = coreColor(128, 128, 128)

@UnderDevelopment
val ColorBlack = coreColor(0, 0, 0)

@UnderDevelopment
val ColorRed = coreColor(255, 0, 0)

@UnderDevelopment
val ColorGreen = coreColor(0, 255, 0)

@UnderDevelopment
val ColorBlue = coreColor(0, 0, 255)

@UnderDevelopment
val ColorYellow = coreColor(255, 255, 0)

@UnderDevelopment
val ColorCyan = coreColor(0, 255, 255)

@UnderDevelopment
val ColorMagenta = coreColor(255, 0, 255)