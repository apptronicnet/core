package net.apptronic.core.android.viewmodel.transitions

import android.view.animation.AccelerateDecelerateInterpolator
import net.apptronic.core.android.viewmodel.transitions.viewswitches.*
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigator

/**
 * Base class for animations implementation. Allows to animate changes for [StackNavigator]
 */
@Deprecated("Replaced by net.apptronic.core.android.anim.*")
open class TransitionBuilder {

    fun getViewSwitchTransition(
        viewSwitch: ViewSwitch,
        transitionSpec: Any?,
        duration: Long
    ): Transition<ViewSwitch> {
        return transitionSpec?.let {
            createViewSwitchTransition(viewSwitch, it, duration)
        } ?: ViewSwitchTransition(null, null)

    }

    /**
     * Called when it needed to create animation transition for adding/removing/switching views
     *
     * @param viewSwitch is object which represents what is switching
     * @param transitionSpec specification of transition
     * @param duration desired animation time
     */
    open fun createViewSwitchTransition(
        viewSwitch: ViewSwitch,
        transitionSpec: Any,
        duration: Long
    ): Transition<ViewSwitch>? {
        return when (transitionSpec) {
            BasicTransition.Fade -> viewSwitch.fadeTransition()
                .withDuration(duration)
            BasicTransition.Next -> viewSwitch.nextTransition()
                .withInterpolator(AccelerateDecelerateInterpolator())
                .withDuration(duration)
            BasicTransition.Previous -> viewSwitch.previousTransition()
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