package net.apptronic.core.android.anim.transformations

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.anim.*

val TranslateXTransformationDescriptor = viewTransformationDescriptor("TranslateXTransformation")

abstract class TranslateXTransformation : ViewTransformation {

    override val descriptor: ViewTransformationDescriptor = TranslateXTransformationDescriptor

    override fun cancelled(target: View, container: View): ViewTransformation {
        return TranslateXToValueTransformation(target.translationX, 0f)
    }

    override fun onReset(target: View, container: View) {
        target.translationX = 0f
    }

}

fun ViewTransformationBuilder.translateXPixels(
    from: Float, to: Float, interpolator: Interpolator? = null
) {
    add(TranslateXToValueTransformation(from, to).interpolateWith(interpolator))
}

fun ViewTransformationBuilder.translateXToSelf(
    from: Float, to: Float, interpolator: Interpolator? = null
) {
    add(TranslateXRelativeToSelfTransformation(from, to).interpolateWith(interpolator))
}

fun ViewTransformationBuilder.translateXToParent(
    from: Float, to: Float, interpolator: Interpolator? = null
) {
    add(TranslateXRelativeToParentTransformation(from, to).interpolateWith(interpolator))
}

class TranslateXToValueTransformation(private val from: Float, private val to: Float) :
    TranslateXTransformation() {

    private var startValue = 0f
    private var endValue = 0f

    override fun onStart(target: View, container: View, intercepting: Boolean) {
        startValue = if (intercepting) target.translationX else from
        endValue = to
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        target.translationX = progress.interpolate(startValue, endValue)
    }

    override fun reversed(): ViewTransformation {
        return TranslateXToValueTransformation(from = to, to = from)
    }

}

class TranslateXRelativeToSelfTransformation(private val from: Float, private val to: Float) :
    TranslateXTransformation() {

    private var startValue = 0f
    private var endValue = 0f

    override fun onStart(target: View, container: View, intercepting: Boolean) {
        startValue = if (intercepting) target.translationX / target.width else from
        endValue = to
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        target.translationX = progress.interpolate(startValue, endValue) * target.width
    }

    override fun reversed(): ViewTransformation {
        return TranslateXRelativeToSelfTransformation(from = to, to = from)
    }

}

class TranslateXRelativeToParentTransformation(private val from: Float, private val to: Float) :
    TranslateXTransformation() {

    private var startValue = 0f
    private var endValue = 0f

    override fun onStart(target: View, container: View, intercepting: Boolean) {
        startValue = if (intercepting) target.translationX / container.width else from
        endValue = to
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        target.translationX = progress.interpolate(startValue, endValue) * container.width
    }

    override fun reversed(): ViewTransformation {
        return TranslateXRelativeToParentTransformation(from = to, to = from)
    }

}

