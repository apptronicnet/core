package net.apptronic.core.android.viewmodel.transitions.transformation

import android.view.View
import android.view.animation.Interpolator
import net.apptronic.core.android.anim.Progress
import net.apptronic.core.android.anim.ViewTransformationDescriptor
import net.apptronic.core.android.anim.interpolate

@Deprecated("Replaced by net.apptronic.core.android.anim.*")
fun TransformationTransitionBuilder.translateYToParent(
    startY: Float, targetY: Float, interpolator: Interpolator? = null
) {
    add(TranslationYToParentTransformation(startY, targetY), interpolator)
}

@Deprecated("Replaced by net.apptronic.core.android.anim.*")
class TranslationYToParentTransformation(
    private val startY: Float, private val targetY: Float
) : Transformation() {

    override val descriptor: ViewTransformationDescriptor = TranslationY

    private var startValue = 0f

    override fun onStart(target: View, container: View, isIntercepted: Boolean) {
        startValue =
            if (isIntercepted && container.width > 0)
                target.translationY / container.width
            else
                startY
    }

    override fun applyTransformation(target: View, container: View, progress: Progress) {
        target.translationY = progress.interpolate(startValue, targetY) * container.height
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

    override fun reversed(): Transformation {
        return TranslationYToParentTransformation(targetY, startY)
    }

}