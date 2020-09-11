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

val TranslateXTransformationDescriptor = transformationDescriptor("TranslateXTransformation")

abstract class TranslateXTransformation : ViewTransformation {

    override val descriptor: TransformationDescriptor = TranslateXTransformationDescriptor

    override fun cancelled(target: View, container: View): ViewTransformation {
        return TranslateXToValueTransformation(target.translationX, 0f)
    }

    override fun onReset(target: View, container: View) {
        target.translationX = 0f
    }

}

fun TransformationBuilder.translateXToSelf(
    from: Float,
    to: Float,
    interpolator: Interpolator? = null
) {
    add(TranslateXRelativeToSelfTransformation(from, to).interpolateWith(interpolator))
}

fun TransformationBuilder.translateXToParent(
    from: Float,
    to: Float,
    interpolator: Interpolator? = null
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

