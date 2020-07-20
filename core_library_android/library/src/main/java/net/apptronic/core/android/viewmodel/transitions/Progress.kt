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

fun Progress.interpolateWith(interpolator: Interpolator?): Progress {
    return interpolator?.getInterpolation(this) ?: this
}