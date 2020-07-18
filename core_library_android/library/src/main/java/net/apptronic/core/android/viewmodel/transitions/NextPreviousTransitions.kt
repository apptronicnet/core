package net.apptronic.core.android.viewmodel.transitions

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

abstract class BasePrevNextTransition : ViewTransition() {

    init {
        interpolator = AccelerateDecelerateInterpolator()
    }

    protected var startTranslationX: Float = 0f

    override fun startTransition(target: View, interceptedTransition: Transition<View>?) {
        if (interceptedTransition == null) {
            target.translationX = 0f
        } else {
            startTranslationX = target.translationX
        }
    }

    override fun completeTransition(target: View, isCompleted: Boolean) {
        target.translationX = 0f
    }

}

class NextEnterTransition(val container: View) : BasePrevNextTransition() {

    override val isFrontTransition: Boolean = true

    override fun startTransition(target: View, interceptedTransition: Transition<View>?) {
        super.startTransition(target, interceptedTransition)
        if (interceptedTransition == null) {
            startTranslationX = container.width.toFloat()
        }
    }

    override fun applyTransition(target: View, progress: Progress) {
        target.translationX = progress.interpolate(startTranslationX, 0f)
    }

}


class NextExitTransition(val container: View) : BasePrevNextTransition() {

    override val isFrontTransition: Boolean = true

    override fun startTransition(target: View, interceptedTransition: Transition<View>?) {
        super.startTransition(target, interceptedTransition)
        if (interceptedTransition == null) {
            startTranslationX = 0f
        }
    }

    override fun applyTransition(target: View, progress: Progress) {
        target.translationX = progress.interpolate(startTranslationX, -container.width.toFloat())
    }

}

class PreviousEnterTransition(val container: View) : BasePrevNextTransition() {

    override fun startTransition(target: View, interceptedTransition: Transition<View>?) {
        super.startTransition(target, interceptedTransition)
        if (interceptedTransition == null) {
            startTranslationX = -container.width.toFloat()
        }
    }

    override fun applyTransition(target: View, progress: Progress) {
        target.translationX = progress.interpolate(startTranslationX, 0f)
    }

}


class PreviousExitTransition(val container: View) : BasePrevNextTransition() {

    override fun startTransition(target: View, interceptedTransition: Transition<View>?) {
        super.startTransition(target, interceptedTransition)
        if (interceptedTransition == null) {
            startTranslationX = 0f
        }
    }


    override fun applyTransition(target: View, progress: Progress) {
        target.translationX = progress.interpolate(startTranslationX, container.width.toFloat())
    }

}