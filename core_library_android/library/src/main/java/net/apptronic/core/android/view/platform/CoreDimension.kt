package net.apptronic.core.android.view.platform

import android.content.Context
import android.content.res.Resources
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.view.dimension.DiscreteCoreDimension

/**
 * Android pixel dimension Int
 */
fun Number.dimensionPixelsInt(resources: Resources): Int = when (this) {
    is DiscreteCoreDimension -> (this.size * resources.displayMetrics.density).toInt()
    else -> this.toInt()
}

/**
 * Android pixel dimension in Float
 */
fun Number.dimensionPixelsInt(context: Context): Int =
    dimensionPixelsInt(context.resources)

fun Entity<Number>.dimensionPixelsInt(resources: Resources): Entity<Int> = map {
    it.dimensionPixelsInt(resources)
}

fun Entity<Number>.dimensionPixelsInt(context: Context): Entity<Int> = map {
    it.dimensionPixelsInt(context)
}

/**
 * Android pixel dimension in Float
 */
fun Number.dimensionPixelsFloat(resources: Resources): Float = when (this) {
    is DiscreteCoreDimension -> (this.size * resources.displayMetrics.density).toFloat()
    else -> this.toFloat()
}

/**
 * Android pixel dimension in Float
 */
fun Number.dimensionPixelsFloat(context: Context): Float =
    dimensionPixelsFloat(context.resources)

fun Entity<Number>.dimensionPixelsFloat(resources: Resources): Entity<Float> = map {
    it.dimensionPixelsFloat(resources)
}

fun Entity<Number>.dimensionPixelsFloat(context: Context): Entity<Float> = map {
    it.dimensionPixelsFloat(context)
}