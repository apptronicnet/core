package net.apptronic.core.android.viewmodel.transitions

import android.view.View

class EmptyViewTransition : ViewTransition() {

    override fun onStartTransition(target: View, interceptedTransition: Transition<View>?) {
        // do nothing
    }

    override fun applyTransition(target: View, progress: Progress) {
        // do nothing
    }

    override fun onCompleteTransition(target: View, isCompleted: Boolean) {
        target.translationX = 0f
        target.translationY = 0f
        target.alpha = 1f
    }

}