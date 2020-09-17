package net.apptronic.core.android.anim.transformations

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.anim.*

val AlphaTransformationDescriptor = viewTransformationDescriptor("AlphaTransformation")

fun ViewTransformationBuilder.alpha(from: Float, to: Float, interpolator: Interpolator? = null) {
    add(AlphaTransformation(from, to).interpolateWith(interpolator))
}

class AlphaTransformation(private val from: Float, private val to: Float) : ViewTransformation {

    override val descriptor: ViewTransformationDescriptor = AlphaTransformationDescriptor

    private var startValue = 0f
    private var endValue = 0f

    override fun onStart(target: View, container: View, intercepting: Boolean) {
        startValue = if (intercepting) target.alpha else from
        endValue = to
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        target.alpha = progress.interpolate(startValue, endValue)
    }

    override fun onReset(target: View, container: View) {
        target.alpha = 1f
    }

    override fun cancelled(target: View, container: View): ViewTransformation {
        return AlphaTransformation(target.alpha, 1f)
    }

    override fun reversed(): ViewTransformation {
        return AlphaTransformation(from = to, to = from)
    }

}