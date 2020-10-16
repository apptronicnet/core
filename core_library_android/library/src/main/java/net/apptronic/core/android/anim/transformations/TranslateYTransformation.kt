package net.apptronic.core.android.anim.transformations

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.anim.*

val TranslateYTransformationDescriptor = viewTransformationDescriptor("TranslateYTransformation")

abstract class TranslateYTransformation : ViewTransformation {

    override val descriptor: ViewTransformationDescriptor = TranslateYTransformationDescriptor

    override fun cancelled(target: View, container: View): ViewTransformation {
        return TranslateYToValueTransformation(target.translationY, 0f)
    }

    override fun onReset(target: View, container: View) {
        target.translationY = 0f
    }

}

fun ViewTransformationBuilder.translateYPixels(
    from: Float, to: Float, interpolator: Interpolator? = null
) {
    add(TranslateYToValueTransformation(from, to).interpolateWith(interpolator))
}

fun ViewTransformationBuilder.translateYToSelf(
    from: Float, to: Float, interpolator: Interpolator? = null
) {
    add(TranslateYRelativeToSelfTransformation(from, to).interpolateWith(interpolator))
}

fun ViewTransformationBuilder.translateYToParent(
    from: Float, to: Float, interpolator: Interpolator? = null
) {
    add(TranslateYRelativeToParentTransformation(from, to).interpolateWith(interpolator))
}

class TranslateYToValueTransformation(private val from: Float, private val to: Float) :
    TranslateYTransformation() {

    private var startValue = 0f
    private var endValue = 0f

    override fun onStart(target: View, container: View, intercepting: Boolean) {
        startValue = if (intercepting) target.translationY else from
        endValue = to
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        target.translationY = progress.interpolate(startValue, endValue)
    }

    override fun reversed(): ViewTransformation {
        return TranslateYToValueTransformation(from = to, to = from)
    }

}

class TranslateYRelativeToSelfTransformation(private val from: Float, private val to: Float) :
    TranslateYTransformation() {

    private var startValue = 0f
    private var endValue = 0f

    override fun onStart(target: View, container: View, intercepting: Boolean) {
        startValue = if (intercepting) target.translationY / target.height else from
        endValue = to
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        target.translationY = progress.interpolate(startValue, endValue) * target.height
    }

    override fun reversed(): ViewTransformation {
        return TranslateYRelativeToSelfTransformation(from = to, to = from)
    }

}

class TranslateYRelativeToParentTransformation(private val from: Float, private val to: Float) :
    TranslateYTransformation() {

    private var startValue = 0f
    private var endValue = 0f

    override fun onStart(target: View, container: View, intercepting: Boolean) {
        startValue = if (intercepting) target.translationY / container.height else from
        endValue = to
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        target.translationY = progress.interpolate(startValue, endValue) * container.height
    }

    override fun reversed(): ViewTransformation {
        return TranslateYRelativeToParentTransformation(from = to, to = from)
    }

}