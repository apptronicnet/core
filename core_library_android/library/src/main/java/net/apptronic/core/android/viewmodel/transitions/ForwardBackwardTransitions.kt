package net.apptronic.core.android.viewmodel.transitions

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

const val FORWARD_BACKWARD_OVERLAP = 0.5f
const val MAX_OVERLAY_ALPHA = 0.7f

fun Drawable.setFloatAlpha(floatAlpha: Float) {
    alpha = (floatAlpha * 255f).toInt()
}

fun Drawable.getFloatAlpha(): Float {
    return alpha.toFloat() / 255f
}

abstract class BaseForwardBackwardTranslation : ViewTransition() {

    init {
        interpolator = AccelerateDecelerateInterpolator()
    }

    protected var startTranslationX: Float = 0f
    protected var startAlpha: Float = 0f

    internal fun withOverlay(target: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            target.foreground = ColorDrawable(Color.BLACK)
        }
    }

    fun setOverlayAlpha(target: View, alpha: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            target.foreground?.setFloatAlpha(alpha)
        }
    }

    fun getOverlayAlpha(target: View): Float {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return target.foreground?.getFloatAlpha() ?: 0f
        }
        return 0f
    }

    override fun onStartTransition(target: View, interceptedTransition: Transition<View>?) {
        if (interceptedTransition != null) {
            startTranslationX = target.translationX
        } else {
            target.translationX = 0f
        }
    }

    override fun onCompleteTransition(target: View, isCompleted: Boolean) {
        target.translationX = 0f
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            target.foreground = null
        }
    }

}

class ForwardEnterTransition(val container: View) : BaseForwardBackwardTranslation() {

    override val isFrontTransition: Boolean = true

    override fun onStartTransition(target: View, interceptedTransition: Transition<View>?) {
        super.onStartTransition(target, interceptedTransition)
        if (interceptedTransition == null) {
            startTranslationX = container.width.toFloat()
        } else {
            startTranslationX = target.translationX
        }
    }

    override fun applyTransition(target: View, progress: Progress) {
        target.translationX = progress.interpolate(startTranslationX, 0f)
    }

}


class ForwardExitTransition(val container: View) : BaseForwardBackwardTranslation() {

    override fun onStartTransition(target: View, interceptedTransition: Transition<View>?) {
        super.onStartTransition(target, interceptedTransition)
        if (interceptedTransition == null) {
            startTranslationX = 0f
            startAlpha = 0f
        } else {
            startTranslationX = target.translationX
            startAlpha = getOverlayAlpha(target)
        }
        withOverlay(target)
    }

    override fun applyTransition(target: View, progress: Progress) {
        setOverlayAlpha(target, progress.interpolate(startAlpha, MAX_OVERLAY_ALPHA))
        target.translationX =
            progress.interpolate(
                startTranslationX,
                -container.width.toFloat() * FORWARD_BACKWARD_OVERLAP
            )
    }

}

class BackwardEnterTransition(val container: View) : BaseForwardBackwardTranslation() {

    override fun onStartTransition(target: View, interceptedTransition: Transition<View>?) {
        super.onStartTransition(target, interceptedTransition)
        if (interceptedTransition == null) {
            startTranslationX = -container.width.toFloat() * FORWARD_BACKWARD_OVERLAP
            startAlpha = MAX_OVERLAY_ALPHA
        } else {
            startTranslationX = target.translationX
            startAlpha = getOverlayAlpha(target)
        }
        withOverlay(target)
    }

    override fun applyTransition(target: View, progress: Progress) {
        setOverlayAlpha(target, progress.interpolate(startAlpha, 0f))
        target.translationX = progress.interpolate(startTranslationX, 0f)
    }

}


class BackwardExitTransition(val container: View) : BaseForwardBackwardTranslation() {

    override val isFrontTransition: Boolean = true

    override fun onStartTransition(target: View, interceptedTransition: Transition<View>?) {
        super.onStartTransition(target, interceptedTransition)
        if (interceptedTransition == null) {
            startTranslationX = 0f
        } else {
            startTranslationX = target.translationX
        }
    }

    override fun applyTransition(target: View, progress: Progress) {
        target.translationX =
            progress.interpolate(startTranslationX, container.width.toFloat())
    }

}