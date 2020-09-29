package net.apptronic.core.android.view.platform

import android.graphics.Color
import net.apptronic.core.view.properties.CoreColor

fun CoreColor.getAndroidColor(): Int {
    return Color.argb(alphaInt, red, green, blue)
}