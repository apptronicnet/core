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

    override fun onCancel(target: View, container: View): ViewTransformation {
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

    override fun onStart(target: View, container: View) {
        startValue = from
        endValue = to
    }

    override fun onIntercept(target: View, container: View) {
        startValue = target.translationX
        endValue = to
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        target.translationX = progress.interpolate(startValue, endValue)
    }

}

class TranslateXRelativeToSelfTransformation(private val from: Float, private val to: Float) :
    TranslateXTransformation() {

    private var startValue = 0f
    private var endValue = 0f

    override fun onStart(target: View, container: View) {
        startValue = from
        endValue = to
    }

    override fun onIntercept(target: View, container: View) {
        startValue = target.translationX / target.width
        endValue = to
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        target.translationX = progress.interpolate(startValue, endValue) * target.width
    }

}

class TranslateXRelativeToParentTransformation(private val from: Float, private val to: Float) :
    TranslateXTransformation() {

    private var startValue = 0f
    private var endValue = 0f

    override fun onStart(target: View, container: View) {
        startValue = from
        endValue = to
    }

    override fun onIntercept(target: View, container: View) {
        startValue = target.translationX / container.width
        endValue = to
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        target.translationX = progress.interpolate(startValue, endValue) * container.width
    }

}