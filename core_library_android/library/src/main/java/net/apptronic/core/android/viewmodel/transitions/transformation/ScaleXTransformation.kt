package net.apptronic.core.android.viewmodel.transitions.transformation

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.interpolate

private val ScaleXDescriptor = transformationDescriptor("ScaleX")

fun TransformationTransitionBuilder.scaleX(
    startScaleX: Float, targetScaleX: Float, interpolator: Interpolator? = null
) {
    add(ScaleXTransformation(startScaleX, targetScaleX), interpolator)
}

class ScaleXTransformation(
    private val startScaleX: Float, private val targetScaleX: Float
) : Transformation() {

    override val descriptor: TransformationDescriptor = ScaleXDescriptor

    override fun onStart(target: View, container: View) {

    }

    override fun applyTransformation(target: View, container: View, progress: Progress) {
        target.scaleX = progress.interpolate(startScaleX, targetScaleX) * container.width
    }

    override fun onClear(target: View) {
        target.scaleX = 1f
    }

    override fun onCancel(target: View, container: View): Transformation {
        return ScaleXTransformation(target.scaleX, 1f)
    }

}