package net.apptronic.core.android.viewmodel.transitions.transformation

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.anim.Progress
import net.apptronic.core.android.anim.ViewTransformationDescriptor
import net.apptronic.core.android.anim.interpolate

@Deprecated("Replaced by net.apptronic.core.android.anim.*")
fun TransformationTransitionBuilder.scaleY(
    startScaleY: Float, targetScaleY: Float, interpolator: Interpolator? = null
) {
    add(ScaleYTransformation(startScaleY, targetScaleY), interpolator)
}

@Deprecated("Replaced by net.apptronic.core.android.anim.*")
class ScaleYTransformation(
    private val startScaleY: Float, private val targetScaleY: Float
) : Transformation() {

    override val descriptor: ViewTransformationDescriptor = ScaleY

    private var startValue = 0f

    override fun onStart(target: View, container: View, isIntercepted: Boolean) {
        startValue = if (isIntercepted) target.scaleY else startScaleY
    }

    override fun applyTransformation(target: View, container: View, progress: Progress) {
        target.scaleY = progress.interpolate(startValue, targetScaleY, 0f, null)
    }

    override fun onClear(target: View) {
        target.scaleY = 1f
    }

    override fun onCancel(target: View, container: View): Transformation {
        return ScaleYTransformation(target.scaleY, 1f)
    }

    override fun reversed(): Transformation {
        return ScaleYTransformation(targetScaleY, startScaleY)
    }

}