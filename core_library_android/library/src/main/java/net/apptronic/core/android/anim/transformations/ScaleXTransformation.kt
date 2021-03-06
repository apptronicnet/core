package net.apptronic.core.android.anim.transformations

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.anim.*

val ScaleXTransformationDescriptor = viewTransformationDescriptor("ScaleXTransformation")

fun ViewTransformationBuilder.scaleX(from: Float, to: Float, interpolator: Interpolator? = null) {
    add(ScaleXTransformation(from, to).interpolateWith(interpolator))
}

class ScaleXTransformation(private val from: Float, private val to: Float) : ViewTransformation {

    override val descriptor: ViewTransformationDescriptor = ScaleXTransformationDescriptor

    private var startValue = 0f
    private var endValue = 0f

    override fun onStart(target: View, container: View, intercepting: Boolean) {
        startValue = if (intercepting) target.scaleX else from
        endValue = to
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        target.scaleX = progress.interpolate(startValue, endValue)
    }

    override fun onReset(target: View, container: View) {
        target.scaleX = 1f
    }

    override fun cancelled(target: View, container: View): ViewTransformation {
        return ScaleXTransformation(target.scaleX, 1f)
    }

    override fun reversed(): ViewTransformation {
        return ScaleXTransformation(from = to, to = from)
    }

}