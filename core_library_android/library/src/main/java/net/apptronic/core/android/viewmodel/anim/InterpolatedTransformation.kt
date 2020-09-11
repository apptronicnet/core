package net.apptronic.core.android.viewmodel.anim

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.interpolateWith

fun ViewTransformation.interpolateWith(interpolator: Interpolator?): ViewTransformation {
    return if (interpolator != null) {
        InterpolatedTransformation(this, interpolator)
    } else {
        this
    }
}

class InterpolatedTransformation internal constructor(
    private val transformation: ViewTransformation, private val interpolator: Interpolator
) : ViewTransformation by transformation {

    override fun onTransform(target: View, container: View, progress: Progress) {
        transformation.onTransform(target, container, progress.interpolateWith(interpolator))
    }

    override fun reversed(): ViewTransformation {
        return transformation.reversed().interpolateWith(ReverseInterpolator(interpolator))
    }

}