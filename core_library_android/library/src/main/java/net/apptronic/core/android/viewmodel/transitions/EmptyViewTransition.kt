package net.apptronic.core.android.viewmodel.transitions

import android.view.View

class EmptyViewTransition : ViewTransition() {

    override fun onTransitionCompleted(target: View) {
        super.onTransitionCompleted(target)
        target.translationX = 0f
        target.translationY = 0f
        target.alpha = 1f
    }

    override fun onTransitionCancelled(target: View) {
        super.onTransitionCancelled(target)
        onTransitionIntercepted(target)
    }

    override fun applyTransition(target: View, progress: Progress) {
        // do nothing
    }

}