package net.apptronic.test.commons_sample_app

import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import net.apptronic.core.android.viewmodel.transitions.Transition
import net.apptronic.core.android.viewmodel.transitions.TransitionBuilder
import net.apptronic.core.android.viewmodel.transitions.ViewSwitch
import net.apptronic.core.android.viewmodel.transitions.transformation.transition
import net.apptronic.core.android.viewmodel.transitions.transformation.translateYToSelf
import net.apptronic.test.commons_sample_app.transition.AppTransition

class AppTransitionBuilder : TransitionBuilder() {

    override fun createViewSwitchTransition(
        viewSwitch: ViewSwitch,
        transitionInfo: Any,
        duration: Long
    ): Transition<ViewSwitch>? {
        return when (transitionInfo) {
            AppTransition.BottomSheet -> viewSwitch.transition {
                enter {
                    translateYToSelf(1f, 0f)
                    interpolator = DecelerateInterpolator()
                }
                exit {
                    translateYToSelf(0f, 1f)
                    interpolator = AccelerateInterpolator()

                }
            }.withDuration(duration)
            else -> super.createViewSwitchTransition(viewSwitch, transitionInfo, duration)
        }
    }

}