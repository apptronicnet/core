package net.apptronic.core.android.viewmodel.transitions.transformation

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.interpolate

private val ScaleYDescriptor = transformationDescriptor("ScaleY")

fun TransformationTransitionBuilder.scaleY(
    startScaleY: Float, targetScaleY: Float, interpolator: Interpolator? = null
) {
    add(ScaleYTransformation(startScaleY, targetScaleY), interpolator)
}

class ScaleYTransformation(
    private val startScaleY: Float, private val targetScaleY: Float
) : Transformation() {

    override val descriptor: TransformationDescriptor = ScaleYDescriptor

    override fun onStart(target: View, container: View) {

    }

    override fun applyTransformation(target: View, container: View, progress: Progress) {
        target.scaleY = progress.interpolate(startScaleY, targetScaleY) * container.width
    }

    override fun onClear(target: View) {
        target.scaleY = 1f
    }

    override fun onCancel(target: View, container: View): Transformation {
        return ScaleYTransformation(target.scaleY, 1f)
    }

}