package net.apptronic.core.android.viewmodel.transitions.transformation

import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator

@Deprecated("Replaced by net.apptronic.core.android.anim.*")
fun Interpolator.reversed(): Interpolator {
    return when (this) {
        is AccelerateInterpolator -> DecelerateInterpolator()
        is DecelerateInterpolator -> AccelerateInterpolator()
        else -> this
    }
}