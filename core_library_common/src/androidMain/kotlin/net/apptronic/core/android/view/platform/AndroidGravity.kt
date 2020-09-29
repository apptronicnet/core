package net.apptronic.core.android.view.platform

import android.view.Gravity
import net.apptronic.core.view.properties.*

fun HorizontalAlignment.getGravityInt(): Int {
    return when (this) {
        ToLeft -> Gravity.LEFT
        ToStart -> Gravity.START
        ToCenter -> Gravity.CENTER_HORIZONTAL
        ToRight -> Gravity.RIGHT
        ToEnd -> Gravity.END
        else -> 0
    }
}

fun VerticalAlignment.getGravityInt(): Int {
    return when (this) {
        ToTop -> Gravity.TOP
        ToCenter -> Gravity.CENTER_VERTICAL
        ToBottom -> Gravity.BOTTOM
        else -> 0
    }
}