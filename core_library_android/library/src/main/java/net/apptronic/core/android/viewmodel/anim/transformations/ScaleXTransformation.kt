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

val ScaleXTransformationDescriptor = transformationDescriptor("ScaleXTransformation")

fun TransformationBuilder.scaleX(from: Float, to: Float, interpolator: Interpolator? = null) {
    add(ScaleXTransformation(from, to).interpolateWith(interpolator))
}

class ScaleXTransformation(private val from: Float, private val to: Float) : ViewTransformation {

    override val descriptor: TransformationDescriptor = ScaleXTransformationDescriptor

    private var startValue = 0f
    private var endValue = 0f

    override fun onStart(target: View, container: View) {
        startValue = from
        endValue = to
    }

    override fun onIntercept(target: View, container: View) {
        startValue = target.scaleX
        endValue = to
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        target.scaleX = progress.interpolate(startValue, endValue)
    }

    override fun onReset(target: View, container: View) {
        target.scaleX = 1f
    }

    override fun onCancel(target: View, container: View): ViewTransformation {
        return ScaleXTransformation(target.scaleX, 1f)
    }

}