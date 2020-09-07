package net.apptronic.core.android.viewmodel.transitions.transformation

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.interpolate

fun TransformationTransitionBuilder.translateXToParent(
    startX: Float, targetX: Float, interpolator: Interpolator? = null
) {
    add(TranslationXToParentTransformation(startX, targetX), interpolator)
}

class TranslationXToParentTransformation(
    private val startX: Float, private val targetX: Float
) : Transformation() {

    override val descriptor: TransformationDescriptor = TranslationX

    private var startValue = 0f

    override fun onStart(target: View, container: View, isIntercepted: Boolean) {
        startValue =
            if (isIntercepted && container.width > 0)
                target.translationX / container.width
            else
                startX
    }

    override fun applyTransformation(target: View, container: View, progress: Progress) {
        target.translationX = progress.interpolate(startValue, targetX) * container.width
    }

    override fun onClear(target: View) {
        target.translationX = 0f
    }

    override fun onCancel(target: View, container: View): Transformation {
        val start = if (container.width > 0) {
            target.translationX / container.width
        } else 0f
        return TranslationXToParentTransformation(start, 0f)
    }

    override fun reversed(): Transformation {
        return TranslationXToParentTransformation(targetX, startX)
    }

}