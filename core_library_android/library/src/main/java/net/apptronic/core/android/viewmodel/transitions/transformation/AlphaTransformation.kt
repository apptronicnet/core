package net.apptronic.core.android.viewmodel.transitions.transformation

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.interpolate

fun TransformationTransitionBuilder.alpha(
    startAlpha: Float, targetAlpha: Float, interpolator: Interpolator? = null
) {
    add(AlphaTransformation(startAlpha, targetAlpha), interpolator)
}

class AlphaTransformation(
    private val startAlpha: Float, private val targetAlpha: Float
) : Transformation() {

    override val descriptor: TransformationDescriptor = Alpha

    private var startValue: Float = 0f

    override fun onStart(target: View, container: View, isIntercepted: Boolean) {
        startValue = if (isIntercepted) target.alpha else startAlpha
    }

    override fun applyTransformation(target: View, container: View, progress: Progress) {
        target.alpha = progress.interpolate(startValue, targetAlpha, 0f, 1f)
    }

    override fun onClear(target: View) {
        target.alpha = 1f
    }

    override fun onCancel(target: View, container: View): Transformation {
        return AlphaTransformation(target.alpha, 1f)
    }

    override fun reversed(): Transformation {
        return AlphaTransformation(targetAlpha, startAlpha)
    }

}