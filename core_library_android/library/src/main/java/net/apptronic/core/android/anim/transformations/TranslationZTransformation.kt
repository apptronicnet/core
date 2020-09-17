package net.apptronic.core.android.anim.transformations

import android.util.TypedValue
import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.anim.*

val TranslationZTransformationDescriptor =
    viewTransformationDescriptor("TranslationZTransformation")

fun ViewTransformationBuilder.translationZPixels(
    from: Float, to: Float, default: Float? = null, interpolator: Interpolator? = null
) {
    add(
        TranslationZTransformation(
            from, to, default ?: target.translationZ
        ).interpolateWith(interpolator)
    )
}

fun ViewTransformationBuilder.translationZDp(
    from: Float, to: Float, default: Float? = null, interpolator: Interpolator? = null
) {
    val fromPixels = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, from, target.resources.displayMetrics
    )
    val toPixels = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, to, target.resources.displayMetrics
    )
    val defaultPixels = if (default != null) {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, default, target.resources.displayMetrics
        )
    } else null
    add(
        TranslationZTransformation(
            fromPixels, toPixels, defaultPixels ?: target.translationZ
        ).interpolateWith(interpolator)
    )
}

class TranslationZTransformation(
    val from: Float, val to: Float, val default: Float
) : ViewTransformation {

    override val descriptor: ViewTransformationDescriptor = TranslationZTransformationDescriptor

    private var startValue = 0f
    private var endValue = 0f

    override fun onStart(target: View, container: View, intercepting: Boolean) {
        startValue = if (intercepting) target.translationZ else from
        endValue = to
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        target.translationZ = progress.interpolate(startValue, endValue)
    }

    override fun onReset(target: View, container: View) {
        target.translationZ = default
    }

    override fun cancelled(target: View, container: View): ViewTransformation {
        return TranslationZTransformation(target.translationZ, default, default)
    }

    override fun reversed(): ViewTransformation {
        return TranslationZTransformation(from = to, to = from, default = default)
    }

}