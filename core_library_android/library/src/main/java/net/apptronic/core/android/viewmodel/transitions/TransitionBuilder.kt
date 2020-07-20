package net.apptronic.core.android.viewmodel.transitions

import android.view.animation.AccelerateDecelerateInterpolator
import net.apptronic.core.android.viewmodel.transitions.siewswitches.*
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigator

/**
 * Base class for animations implementation. Allows to animate changes for [StackNavigator]
 */
open class TransitionBuilder {

    fun getViewSwitchTransition(
        viewSwitch: ViewSwitch,
        transition: Any?,
        duration: Long
    ): Transition<ViewSwitch> {
        return transition?.let {
            createViewSwitchTransition(viewSwitch, it, duration)
        } ?: ViewSwitchTransition(null, null)

    }

    /**
     * Called when it needed to create animation transition for adding/removing/switching views
     *
     * @param viewSwitch is object which represents what is switching
     * @param transitionInfo specification of transition
     * @param duration desired animation time
     */
    open fun createViewSwitchTransition(
        viewSwitch: ViewSwitch,
        transitionInfo: Any,
        duration: Long
    ): Transition<ViewSwitch>? {
        return when (transitionInfo) {
            BasicTransition.Fade -> viewSwitch.fadeTransition()
                .withDuration(duration)
            BasicTransition.Next -> viewSwitch.nextTransition()
                .withInterpolator(AccelerateDecelerateInterpolator())
                .withDuration(duration)
            BasicTransition.Fade -> viewSwitch.previousTransition()
                .withInterpolator(AccelerateDecelerateInterpolator())
                .withDuration(duration)
            BasicTransition.Forward -> viewSwitch.forwardTransition()
                .withInterpolator(AccelerateDecelerateInterpolator())
                .withDuration(duration)
            BasicTransition.Backward -> viewSwitch.backwardTransition()
                .withInterpolator(AccelerateDecelerateInterpolator())
                .withDuration(duration)
            else -> null
        }
    }

}