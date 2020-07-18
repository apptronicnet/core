package net.apptronic.test.commons_sample_app.transitions

import android.view.View
import net.apptronic.core.android.viewmodel.transitions.Progress
import net.apptronic.core.android.viewmodel.transitions.Transition
import net.apptronic.core.android.viewmodel.transitions.ViewTransition
import net.apptronic.core.android.viewmodel.transitions.interpolate

class EnterSlideUp : ViewTransition() {

    private var startTranslationY = 0f

    override fun startTransition(target: View, interceptedTransition: Transition<View>?) {
        if (interceptedTransition == null) {
            startTranslationY = 1f
        } else {
            startTranslationY = target.translationY / target.height
        }
    }

    override fun applyTransition(target: View, progress: Progress) {
        target.translationY = progress.interpolate(startTranslationY, 0f) * target.height
    }

    override fun completeTransition(target: View, isCompleted: Boolean) {
        target.translationY = 0f
    }

}