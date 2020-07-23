package net.apptronic.core.android.viewmodel.transitions.transformation

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.interpolate

private val ForegroundWithAlphaDescriptor = transformationDescriptor("ForegroundWithAlpha")

fun TransformationTransitionBuilder.foregroundWithAlpha(
    startAlpha: Float, targetAlpha: Float, foreground: Drawable?, interpolator: Interpolator? = null
) {
    if (foreground != null) {
        add(ForegroundWithAlphaTransformation(startAlpha, targetAlpha, foreground), interpolator)
    }
}

class ForegroundWithAlphaTransformation(
    private val startAlpha: Float,
    private val targetAlpha: Float,
    private val foreground: Drawable
) : Transformation() {

    override val descriptor: TransformationDescriptor = ForegroundWithAlphaDescriptor

    private var startValue = 0f

    override fun onStart(target: View, container: View, isIntercepted: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            target.foreground = foreground
            startValue = if (isIntercepted) (target.foreground?.alpha?.toFloat()
                ?: 0f) / 255f else startAlpha
        } else {
            startValue = 0f
        }
    }

    override fun applyTransformation(target: View, container: View, progress: Progress) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            target.foreground?.alpha =
                (progress.interpolate(startValue, targetAlpha) * 255f).toInt()
        }
    }

    override fun onClear(target: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            target.foreground?.alpha = 255
            target.foreground = null
        }
    }

    override fun onCancel(target: View, container: View): Transformation {
        val startAlpha = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            target.foreground?.alpha ?: 255
        } else 255
        return ForegroundWithAlphaTransformation(startAlpha.toFloat() / 255f, 1f, foreground)
    }

    override fun reversed(): Transformation {
        return ForegroundWithAlphaTransformation(targetAlpha, startAlpha, foreground)
    }

}