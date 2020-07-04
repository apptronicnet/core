package net.apptronic.core.android.viewmodel.transitions

import android.view.View

class FadeInTransition : Transition() {

    private var startAlpha: Float = 0f

    override fun onTransitionIntercepted(target: View) {
        super.onTransitionIntercepted(target)
        startAlpha = target.alpha
    }

    override fun onTransitionStarted(target: View) {
        super.onTransitionStarted(target)
        startAlpha = 0f
    }

    override fun applyTransition(target: View, progress: Progress) {
        target.alpha = progress.interpolate(startAlpha, 1f)
    }

    override fun onTransitionCompleted(target: View) {
        super.onTransitionCompleted(target)
        target.alpha = 1f
    }

    override fun onTransitionCancelled(target: View) {
        super.onTransitionCancelled(target)
        target.alpha = 1f
    }

}

class FadeOutTransition : Transition() {

    private var startAlpha: Float = 0f

    override fun onTransitionIntercepted(target: View) {
        super.onTransitionIntercepted(target)
        startAlpha = target.alpha
    }

    override fun onTransitionStarted(target: View) {
        super.onTransitionStarted(target)
        startAlpha = 1f
    }

    override fun applyTransition(target: View, progress: Progress) {
        target.alpha = progress.interpolate(startAlpha, 0f)
    }

    override fun onTransitionCompleted(target: View) {
        super.onTransitionCompleted(target)
        target.alpha = 1f
    }

    override fun onTransitionCancelled(target: View) {
        super.onTransitionCancelled(target)
        target.alpha = 1f
    }

}