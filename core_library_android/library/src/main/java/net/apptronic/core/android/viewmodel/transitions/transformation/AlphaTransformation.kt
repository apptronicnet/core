package net.apptronic.core.android.viewmodel.transitions.transformation

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.anim.Progress
import net.apptronic.core.android.anim.ViewTransformationDescriptor
import net.apptronic.core.android.anim.interpolate

@Deprecated("Replaced by net.apptronic.core.android.anim.*")
fun TransformationTransitionBuilder.alpha(
    startAlpha: Float, targetAlpha: Float, interpolator: Interpolator? = null
) {
    add(AlphaTransformation(startAlpha, targetAlpha), interpolator)
}

@Deprecated("Replaced by net.apptronic.core.android.anim.*")
class AlphaTransformation(
    private val startAlpha: Float, private val targetAlpha: Float
) : Transformation() {

    override val descriptor: ViewTransformationDescriptor = Alpha

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