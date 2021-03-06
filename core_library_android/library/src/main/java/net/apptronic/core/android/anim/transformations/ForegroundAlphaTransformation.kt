package net.apptronic.core.android.anim.transformations

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.anim.*

val ForegroundAlphaTransformationDescriptor =
    viewTransformationDescriptor("ForegroundAlphaTransformation")

fun ViewTransformationBuilder.foregroundAlpha(
    from: Float,
    to: Float,
    foreground: Drawable,
    interpolator: Interpolator? = null
) {
    add(ForegroundAlphaTransformation(from, to, foreground).interpolateWith(interpolator))
}

class ForegroundAlphaTransformation(
    private val from: Float,
    private val to: Float,
    private val foreground: Drawable
) : ViewTransformation {

    override val descriptor: ViewTransformationDescriptor = ForegroundAlphaTransformationDescriptor

    private var startValue = 0f
    private var endValue = 0f

    private fun View.setForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            foreground = this@ForegroundAlphaTransformation.foreground
        }
    }

    override fun onStart(target: View, container: View, intercepting: Boolean) {
        startValue = if (intercepting) {
            from
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && target.foreground != null) {
                target.foreground!!.alpha.toFloat() / 255f
            } else {
                from
            }
        }
        endValue = to
        target.setForeground()
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            foreground.alpha = (progress.interpolate(startValue, endValue) * 255f).toInt()
        }
    }

    override fun onReset(target: View, container: View) {
        foreground.alpha = 255
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            target.foreground = null
        }
    }

    override fun cancelled(target: View, container: View): ViewTransformation {
        return ForegroundAlphaTransformation(foreground.alpha.toFloat() / 255f, 0f, foreground)
    }

    override fun reversed(): ViewTransformation {
        return ForegroundAlphaTransformation(from = to, to = from, foreground)
    }

}