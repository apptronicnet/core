package net.apptronic.core.android.anim.transformations

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.anim.TransformationBuilder
import net.apptronic.core.android.anim.ViewTransformation
import net.apptronic.core.android.anim.interpolateWith
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.interpolate
import net.apptronic.core.android.viewmodel.transitions.transformation.TransformationDescriptor
import net.apptronic.core.android.viewmodel.transitions.transformation.transformationDescriptor

val RotateTransformationDescriptor = transformationDescriptor("RotateTransformation")

fun TransformationBuilder.rotate(from: Float, to: Float, interpolator: Interpolator? = null) {
    add(RotateTransformation(from, to).interpolateWith(interpolator))
}

class RotateTransformation(private val from: Float, private val to: Float) : ViewTransformation {

    override val descriptor: TransformationDescriptor = RotateTransformationDescriptor

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