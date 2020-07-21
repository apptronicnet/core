package net.apptronic.core.android.viewmodel.transitions.transformation

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.interpolate

fun TransformationTransitionBuilder.translateXToSelf(
    startX: Float, targetX: Float, interpolator: Interpolator? = null
) {
    add(TranslationXToSelfTransformation(startX, targetX), interpolator)
}

class TranslationXToSelfTransformation(
    private val startX: Float, private val targetX: Float
) : Transformation() {

    override val descriptor: TransformationDescriptor = TranslationX

    override fun onStart(target: View, container: View) {

    }

    override fun applyTransformation(target: View, container: View, progress: Progress) {
        target.translationX = progress.interpolate(startX, targetX) * target.width
    }

    override fun onClear(target: View) {
        target.translationX = 0f
    }

    override fun onCancel(target: View, container: View): Transformation {
        val start = if (target.width > 0) {
            target.translationX / target.width
        } else 0f
        return TranslationXToSelfTransformation(start, 0f)
    }

}