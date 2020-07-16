package net.apptronic.core.android.viewmodel.transitions

import android.view.View

class FadeInTransition : ViewTransition() {

    private var startAlpha: Float = 0f

    override fun onStartTransition(target: View, interceptedTransition: Transition<View>?) {
        if (interceptedTransition == null) {
            startAlpha = 0f
        } else {
            startAlpha = target.alpha
        }
    }

    override fun applyTransition(target: View, progress: Progress) {
        target.alpha = progress.interpolate(startAlpha, 1f)
    }

    override fun onCompleteTransition(target: View, isCompleted: Boolean) {
        target.alpha = 1f
    }

}

class FadeOutTransition : ViewTransition() {

    private var startAlpha: Float = 0f

    override fun onStartTransition(target: View, interceptedTransition: Transition<View>?) {
        if (interceptedTransition == null) {
            startAlpha = 1f
        } else {
            startAlpha = target.alpha
        }
    }

    override fun applyTransition(target: View, progress: Progress) {
        target.alpha = progress.interpolate(startAlpha, 0f)
    }

    override fun onCompleteTransition(target: View, isCompleted: Boolean) {
        target.alpha = 1f
    }

}