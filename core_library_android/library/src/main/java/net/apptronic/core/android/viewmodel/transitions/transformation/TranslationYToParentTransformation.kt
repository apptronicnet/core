package net.apptronic.core.android.viewmodel.transitions.transformation

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.interpolate

fun TransformationTransitionBuilder.translateYToParent(
    startY: Float, targetY: Float, interpolator: Interpolator? = null
) {
    add(TranslationYToParentTransformation(startY, targetY), interpolator)
}

class TranslationYToParentTransformation(
    private val startY: Float, private val targetY: Float
) : Transformation() {

    override val descriptor: TransformationDescriptor = TranslationY

    override fun onStart(target: View, container: View) {

    }

    override fun applyTransformation(target: View, container: View, progress: Progress) {
        target.translationY = progress.interpolate(startY, targetY) * container.height
    }

    override fun onClear(target: View) {
        target.translationY = 0f
    }

    override fun onCancel(target: View, container: View): Transformation {
        val start = if (container.height > 0) {
            target.translationY / container.height
        } else 0f
        return TranslationYToSelfTransformation(start, 0f)
    }

}