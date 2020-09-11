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

val TranslateYTransformationDescriptor = transformationDescriptor("TranslateYTransformation")

abstract class TranslateYTransformation : ViewTransformation {

    override val descriptor: TransformationDescriptor = TranslateYTransformationDescriptor

    override fun onCancel(target: View, container: View): ViewTransformation {
        return TranslateYToValueTransformation(target.translationY, 0f)
    }

    override fun onReset(target: View, container: View) {
        target.translationY = 0f
    }

}

fun TransformationBuilder.translateYToSelf(
    from: Float,
    to: Float,
    interpolator: Interpolator? = null
) {
    add(TranslateYRelativeToSelfTransformation(from, to).interpolateWith(interpolator))
}

fun TransformationBuilder.translateYToParent(
    from: Float,
    to: Float,
    interpolator: Interpolator? = null
) {
    add(TranslateYRelativeToParentTransformation(from, to).interpolateWith(interpolator))
}

class TranslateYToValueTransformation(private val from: Float, private val to: Float) :
    TranslateYTransformation() {

    private var startValue = 0f
    private var endValue = 0f

    override fun onStart(target: View, container: View) {
        startValue = from
        endValue = to
    }

    override fun onIntercept(target: View, container: View) {
        startValue = target.translationY
        endValue = to
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        target.translationY = progress.interpolate(startValue, endValue)
    }

}

class TranslateYRelativeToSelfTransformation(private val from: Float, private val to: Float) :
    TranslateYTransformation() {

    private var startValue = 0f
    private var endValue = 0f

    override fun onStart(target: View, container: View) {
        startValue = from
        endValue = to
    }

    override fun onIntercept(target: View, container: View) {
        startValue = target.translationY / target.width
        endValue = to
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        target.translationY = progress.interpolate(startValue, endValue) * target.width
    }

}

class TranslateYRelativeToParentTransformation(private val from: Float, private val to: Float) :
    TranslateYTransformation() {

    private var startValue = 0f
    private var endValue = 0f

    override fun onStart(target: View, container: View) {
        startValue = from
        endValue = to
    }

    override fun onIntercept(target: View, container: View) {
        startValue = target.translationY / container.width
        endValue = to
    }

    override fun onTransform(target: View, container: View, progress: Progress) {
        target.translationY = progress.interpolate(startValue, endValue) * container.width
    }

}