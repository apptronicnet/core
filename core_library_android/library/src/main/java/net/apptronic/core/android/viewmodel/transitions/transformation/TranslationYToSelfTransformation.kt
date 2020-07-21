package net.apptronic.core.android.viewmodel.transitions.transformation

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.interpolate

fun TransformationTransitionBuilder.translateYToSelf(
    startY: Float, targetY: Float, interpolator: Interpolator? = null
) {
    add(TranslationYToSelfTransformation(startY, targetY), interpolator)
}

class TranslationYToSelfTransformation(
    private val startY: Float, private val targetY: Float
) : Transformation() {

    override val descriptor: TransformationDescriptor = TranslationY

    override fun onStart(target: View, container: View) {

    }

    override fun applyTransformation(target: View, container: View, progress: Progress) {
        target.translationY = progress.interpolate(startY, targetY) * target.height
    }

    override fun onClear(target: View) {
        target.translationY = 0f
    }

    override fun onCancel(target: View, container: View): Transformation {
        val start = if (target.height > 0) {
            target.translationY / target.height
        } else 0f
        return TranslationYToSelfTransformation(start, 0f)
    }

}