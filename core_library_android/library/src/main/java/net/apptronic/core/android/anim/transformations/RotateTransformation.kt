package net.apptronic.core.android.anim.transformations

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.anim.*

val RotateTransformationDescriptor = viewTransformationDescriptor("RotateTransformation")

fun ViewTransformationBuilder.rotate(from: Float, to: Float, interpolator: Interpolator? = null) {
    add(RotateTransformation(from, to).interpolateWith(interpolator))
}

class RotateTransformation(private val from: Float, private val to: Float) : ViewTransformation {

    override val descriptor: ViewTransformationDescriptor = RotateTransformationDescriptor

    private var startValue = 0f
    private var endValue = 0f

    override fun onStart(target: View, container: View, intercepting: Boolean) {
        startValue = if (intercepting) target.rotation else from
        endValue = to
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        target.rotation = progress.interpolate(startValue, endValue)
    }

    override fun onReset(target: View, container: View) {
        target.rotation = 0f
    }

    override fun cancelled(target: View, container: View): ViewTransformation {
        return RotateTransformation(target.rotation, 0f)
    }

    override fun reversed(): ViewTransformation {
        return RotateTransformation(from = to, to = from)
    }

}