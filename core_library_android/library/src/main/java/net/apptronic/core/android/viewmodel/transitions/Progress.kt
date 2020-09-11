package net.apptronic.core.android.viewmodel.transitions

import android.view.animation.Interpolator

typealias Progress = Float

fun Progress.interpolate(start: Int, target: Int): Int {
    if (this <= 0f) {
        return start
    }
    if (this >= 1f) {
        return target
    }
    val range = target - start
    val change = (range * this).toInt()
    return start + change
}

fun Progress.reverse(): Progress {
    return 1f - this
}

fun Progress.interpolate(start: Int, target: Int, min: Int?, max: Int?): Int {
    val interpolation = interpolate(start, target)
    return when {
        min != null && interpolation < min -> min
        max != null && interpolation > max -> max
        else -> interpolation
    }
}

fun Progress.interpolate(start: Long, target: Long): Long {
    if (this <= 0f) {
        return start
    }
    if (this >= 1f) {
        return target
    }
    val range = target - start
    val change = (range * this).toLong()
    return start + change
}

fun Progress.interpolate(start: Long, target: Long, min: Long?, max: Long?): Long {
    val interpolation = interpolate(start, target)
    return when {
        min != null && interpolation < min -> min
        max != null && interpolation > max -> max
        else -> interpolation
    }
}

fun Progress.interpolate(start: Float, target: Float): Float {
    if (this <= 0f) {
        return start
    }
    if (this >= 1f) {
        return target
    }
    val range = target - start
    val change = range * this
    return start + change
}

fun Progress.interpolate(start: Float, target: Float, min: Float?, max: Float?): Float {
    val interpolation = interpolate(start, target)
    return when {
        min != null && interpolation < min -> min
        max != null && interpolation > max -> max
        else -> interpolation
    }
}

fun Progress.interpolate(start: Double, target: Double): Double {
    if (this <= 0f) {
        return start
    }
    if (this >= 1f) {
        return target
    }
    val range = target - start
    val change = range * this.toDouble()
    return start + change
}

fun Progress.interpolate(start: Double, target: Double, min: Double?, max: Double?): Double {
    val interpolation = interpolate(start, target)
    return when {
        min != null && interpolation < min -> min
        max != null && interpolation > max -> max
        else -> interpolation
    }
}

fun Progress.interpolateWith(interpolator: Interpolator?): Progress {
    return interpolator?.getInterpolation(this) ?: this
}