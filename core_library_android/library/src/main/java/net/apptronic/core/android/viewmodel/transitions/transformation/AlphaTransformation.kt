package net.apptronic.core.android.viewmodel.transitions.transformation

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.interpolate

private val AlphaDescriptor = transformationDescriptor("Alpha")

fun TransformationTransitionBuilder.alpha(
    startAlpha: Float, targetAlpha: Float, interpolator: Interpolator? = null
) {
    add(AlphaTransformation(startAlpha, targetAlpha), interpolator)
}

class AlphaTransformation(
    private val startAlpha: Float, private val targetAlpha: Float
) : Transformation() {

    override val descriptor: TransformationDescriptor = AlphaDescriptor

    override fun onStart(target: View, container: View) {

    }

    override fun applyTransformation(target: View, container: View, progress: Progress) {
        target.alpha = progress.interpolate(startAlpha, targetAlpha)
    }

    override fun onClear(target: View) {
        target.alpha = 1f
    }

    override fun onCancel(target: View, container: View): Transformation {
        return AlphaTransformation(target.alpha, 1f)
    }

}