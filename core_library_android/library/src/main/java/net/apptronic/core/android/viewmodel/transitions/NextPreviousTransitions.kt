package net.apptronic.core.android.viewmodel.transitions

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

abstract class BasePrevNextTransition : ViewTransition() {

    init {
        interpolator = AccelerateDecelerateInterpolator()
    }

    protected var startTranslationX: Float = 0f

    override fun onTransitionIntercepted(target: View) {
        super.onTransitionIntercepted(target)
        startTranslationX = target.translationX
    }

    override fun onTransitionCompleted(target: View) {
        super.onTransitionCompleted(target)
        target.translationX = 0f
    }

    override fun onTransitionCancelled(target: View) {
        super.onTransitionCancelled(target)
        target.translationX = 0f
    }

}

class NextEnterTransition(val container: View) : BasePrevNextTransition() {

    override val isFrontTransition: Boolean = true

    override fun onTransitionStarted(target: View) {
        super.onTransitionStarted(target)
        startTranslationX = container.width.toFloat()
    }

    override fun applyTransition(target: View, progress: Progress) {
        target.translationX = progress.interpolate(startTranslationX, 0f)
    }

}


class NextExitTransition(val container: View) : BasePrevNextTransition() {

    override val isFrontTransition: Boolean = true

    override fun onTransitionStarted(target: View) {
        super.onTransitionStarted(target)
        startTranslationX = 0f
    }

    override fun applyTransition(target: View, progress: Progress) {
        target.translationX = progress.interpolate(startTranslationX, -container.width.toFloat())
    }

}

class PreviousEnterTransition(val container: View) : BasePrevNextTransition() {

    override fun onTransitionStarted(target: View) {
        super.onTransitionStarted(target)
        startTranslationX = -container.width.toFloat()
    }

    override fun applyTransition(target: View, progress: Progress) {
        target.translationX = progress.interpolate(startTranslationX, 0f)
    }

}


class PreviousExitTransition(val container: View) : BasePrevNextTransition() {

    override fun onTransitionStarted(target: View) {
        super.onTransitionStarted(target)
        startTranslationX = 0f
    }

    override fun applyTransition(target: View, progress: Progress) {
        target.translationX = progress.interpolate(startTranslationX, container.width.toFloat())
    }

}