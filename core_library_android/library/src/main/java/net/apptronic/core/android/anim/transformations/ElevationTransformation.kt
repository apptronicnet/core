package net.apptronic.core.android.anim.transformations

import android.util.TypedValue
import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.anim.*

val ElevationTransformationDescriptor = viewTransformationDescriptor("ElevationTransformation")

fun ViewTransformationBuilder.elevationPixels(
    from: Float, to: Float, default: Float? = null, interpolator: Interpolator? = null
) {
    add(
        ElevationTransformation(
            from, to, default ?: target.elevation
        ).interpolateWith(interpolator)
    )
}

fun ViewTransformationBuilder.elevationDp(
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
        ElevationTransformation(
            fromPixels, toPixels, defaultPixels ?: target.elevation
        ).interpolateWith(interpolator)
    )
}

class ElevationTransformation(
    val from: Float, val to: Float, val default: Float
) : ViewTransformation {

    override val descriptor: ViewTransformationDescriptor = ElevationTransformationDescriptor

    private var startValue = 0f
    private var endValue = 0f

    override fun onStart(target: View, container: View, intercepting: Boolean) {
        startValue = if (intercepting) target.elevation else from
        endValue = to
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        target.elevation = progress.interpolate(startValue, endValue)
    }

    override fun onReset(target: View, container: View) {
        target.elevation = default
    }

    override fun cancelled(target: View, container: View): ViewTransformation {
        return ElevationTransformation(target.elevation, default, default)
    }

    override fun reversed(): ViewTransformation {
        return ElevationTransformation(from = to, to = from, default = default)
    }

}