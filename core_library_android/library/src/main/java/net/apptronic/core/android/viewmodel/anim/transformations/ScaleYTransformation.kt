package net.apptronic.core.android.viewmodel.anim.transformations

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.viewmodel.anim.TransformationBuilder
import net.apptronic.core.android.viewmodel.anim.ViewTransformation
import net.apptronic.core.android.viewmodel.anim.interpolateWith
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.interpolate
import net.apptronic.core.android.viewmodel.transitions.transformation.TransformationDescriptor
import net.apptronic.core.android.viewmodel.transitions.transformation.transformationDescriptor

val ScaleYTransformationDescriptor = transformationDescriptor("ScaleYTransformation")

fun TransformationBuilder.scaleY(from: Float, to: Float, interpolator: Interpolator? = null) {
    add(ScaleYTransformation(from, to).interpolateWith(interpolator))
}

class ScaleYTransformation(private val from: Float, private val to: Float) : ViewTransformation {

    override val descriptor: TransformationDescriptor = ScaleYTransformationDescriptor

    private var startValue = 0f
    private var endValue = 0f

    override fun onStart(target: View, container: View, intercepting: Boolean) {
        startValue = if (intercepting) target.scaleY else from
        endValue = to
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        target.scaleY = progress.interpolate(startValue, endValue)
    }

    override fun onReset(target: View, container: View) {
        target.scaleY = 1f
    }

    override fun cancelled(target: View, container: View): ViewTransformation {
        return ScaleYTransformation(target.scaleY, 1f)
    }

    override fun reversed(): ViewTransformation {
        return ScaleYTransformation(from = to, to = from)
    }

}