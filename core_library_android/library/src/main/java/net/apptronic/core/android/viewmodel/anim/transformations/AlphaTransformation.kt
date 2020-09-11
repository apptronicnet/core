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

val AlphaTransformationDescriptor = transformationDescriptor("AlphaTransformation")

fun TransformationBuilder.alpha(from: Float, to: Float, interpolator: Interpolator? = null) {
    add(AlphaTransformation(from, to).interpolateWith(interpolator))
}

class AlphaTransformation(private val from: Float, private val to: Float) : ViewTransformation {

    override val descriptor: TransformationDescriptor = AlphaTransformationDescriptor

    private var startValue = 0f
    private var endValue = 0f

    override fun onStart(target: View, container: View) {
        startValue = from
        endValue = to
    }

    override fun onIntercept(target: View, container: View) {
        startValue = target.alpha
        endValue = to
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        target.alpha = progress.interpolate(startValue, endValue)
    }

    override fun onReset(target: View, container: View) {
        target.alpha = 1f
    }

    override fun onCancel(target: View, container: View): ViewTransformation {
        return AlphaTransformation(target.alpha, 1f)
    }

}