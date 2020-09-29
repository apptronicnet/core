package net.apptronic.core.android.view.platform

import android.annotation.SuppressLint
import android.view.Gravity
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.LayoutAlignment
import net.apptronic.core.view.properties.VerticalAlignment

@SuppressLint("RtlHardcoded")
fun HorizontalAlignment.getGravityInt(): Int {
    return when (this) {
        LayoutAlignment.ToLeft -> Gravity.LEFT
        LayoutAlignment.ToStart -> Gravity.START
        LayoutAlignment.ToCenter -> Gravity.CENTER_HORIZONTAL
        LayoutAlignment.ToRight -> Gravity.RIGHT
        LayoutAlignment.ToEnd -> Gravity.END
        else -> 0
    }
}

fun VerticalAlignment.getGravityInt(): Int {
    return when (this) {
        LayoutAlignment.ToTop -> Gravity.TOP
        LayoutAlignment.ToCenter -> Gravity.CENTER_VERTICAL
        LayoutAlignment.ToBottom -> Gravity.BOTTOM
        else -> 0
    }
}