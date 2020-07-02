package net.apptronic.core.android.viewmodel.transitions

import android.view.View

class EmptyTransition : Transition() {

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

}