package net.apptronic.core.android.viewmodel.transitions.transformation

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.anim.Progress
import net.apptronic.core.android.anim.ViewTransformationDescriptor
import net.apptronic.core.android.anim.interpolate

@Deprecated("Replaced by net.apptronic.core.android.anim.*")
fun TransformationTransitionBuilder.scaleX(
    startScaleX: Float, targetScaleX: Float, interpolator: Interpolator? = null
) {
    add(ScaleXTransformation(startScaleX, targetScaleX), interpolator)
}

@Deprecated("Replaced by net.apptronic.core.android.anim.*")
class ScaleXTransformation(
    private val startScaleX: Float, private val targetScaleX: Float
) : Transformation() {

    override val descriptor: ViewTransformationDescriptor = ScaleX

    private var startValue = 0f

    override fun onStart(target: View, container: View, isIntercepted: Boolean) {
        startValue = if (isIntercepted) target.scaleX else startScaleX
    }

    override fun applyTransformation(target: View, container: View, progress: Progress) {
        target.scaleX = progress.interpolate(startValue, targetScaleX, 0f, null)
    }

    override fun onClear(target: View) {
        target.scaleX = 1f
    }

    override fun onCancel(target: View, container: View): Transformation {
        return ScaleXTransformation(target.scaleX, 1f)
    }

    override fun reversed(): Transformation {
        return ScaleXTransformation(targetScaleX, startScaleX)
    }

}