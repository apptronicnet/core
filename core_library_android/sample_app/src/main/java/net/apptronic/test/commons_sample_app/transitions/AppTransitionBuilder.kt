package net.apptronic.test.commons_sample_app.transitions

import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import net.apptronic.core.android.viewmodel.transitions.Transition
import net.apptronic.core.android.viewmodel.transitions.TransitionBuilder
import net.apptronic.test.commons_sample_app.transition.AppTransition

class AppTransitionBuilder : TransitionBuilder() {

    override fun enterTransition(
        container: ViewGroup,
        view: View,
        transition: Any,
        duration: Long
    ): Transition<View>? {
        return when (transition) {
            AppTransition.BottomSheet -> {
                EnterSlideUp().withInterpolator(DecelerateInterpolator())
                    .withDuration(duration)
            }
            else -> super.enterTransition(container, view, transition, duration)
        }
    }

    override fun exitTransition(
        container: ViewGroup,
        view: View,
        transition: Any,
        duration: Long
    ): Transition<View>? {
        return when (transition) {
            AppTransition.BottomSheet -> {
                ExitSlideDown().withInterpolator(AccelerateInterpolator())
                    .withDuration(duration)
            }
            else -> super.exitTransition(container, view, transition, duration)
        }
    }

}