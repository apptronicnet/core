package net.apptronic.core.android.view.platform

import android.graphics.Color
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.view.properties.CoreColor

val CoreColor.colorInt: Int
    get() = Color.argb(alphaInt, red, green, blue)

fun Entity<CoreColor>.colorInt(): Entity<Int> = map {
    it.colorInt
}