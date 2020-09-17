package net.apptronic.core.android.viewmodel.transitions

import android.view.View
import net.apptronic.core.android.anim.Progress

@Deprecated("Replaced by net.apptronic.core.android.anim.*")
class EmptyViewTransition : ViewTransition() {

    override fun startTransition(target: View, interceptedTransition: Transition<View>?) {
        // do nothing
    }

    override fun applyTransition(target: View, progress: Progress) {
        // do nothing
    }

    override fun completeTransition(target: View, isCompleted: Boolean) {
        // do nothing
    }

}