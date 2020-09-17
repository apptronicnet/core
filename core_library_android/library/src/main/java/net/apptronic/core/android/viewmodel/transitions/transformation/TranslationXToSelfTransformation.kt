package net.apptronic.core.android.viewmodel.transitions.transformation

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.anim.Progress
import net.apptronic.core.android.anim.ViewTransformationDescriptor
import net.apptronic.core.android.anim.interpolate

@Deprecated("Replaced by net.apptronic.core.android.anim.*")
fun TransformationTransitionBuilder.translateXToSelf(
    startX: Float, targetX: Float, interpolator: Interpolator? = null
) {
    add(TranslationXToSelfTransformation(startX, targetX), interpolator)
}

@Deprecated("Replaced by net.apptronic.core.android.anim.*")
class TranslationXToSelfTransformation(
    private val startX: Float, private val targetX: Float
) : Transformation() {

    override val descriptor: ViewTransformationDescriptor = TranslationX

    private var startValue = 0f

    override fun onStart(target: View, container: View, isIntercepted: Boolean) {
        startValue =
            if (isIntercepted && target.width > 0)
                target.translationX / target.width
            else
                startX
    }

    override fun applyTransformation(target: View, container: View, progress: Progress) {
        target.translationX = progress.interpolate(startValue, targetX) * target.width
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

    override fun reversed(): Transformation {
        return TranslationXToSelfTransformation(targetX, startX)
    }

}