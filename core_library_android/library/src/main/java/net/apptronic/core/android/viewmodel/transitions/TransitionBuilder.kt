package net.apptronic.core.android.viewmodel.transitions

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigator

/**
 * Base class for animations implementation. Allows to animate changes for [StackNavigator]
 */
open class TransitionBuilder {

    fun getEnterTransition(
        container: ViewGroup, view: View, transition: Any?, duration: Long
    ): Transition {
        return transition?.let {
            enterTransition(container, view, it, duration)
        } ?: EmptyTransition()
    }

    fun getExitTransition(
        container: ViewGroup, view: View, transition: Any?, duration: Long
    ): Transition {
        return transition?.let {
            exitTransition(container, view, it, duration)
        } ?: EmptyTransition()
    }

    /**
     * Called when [ViewBinderStackAdapter] adds [view] to [container]
     *
     * @param container in which [view] added
     * @param view to be added and animated
     * @param transition specification of animation
     * @param duration default time, set for animations in [ViewBinderStackAdapter]
     */
    open fun enterTransition(
        container: ViewGroup, view: View, transition: Any, duration: Long
    ): Transition? {
        return when (transition) {
            BasicTransition.Fade -> FadeInTransition()
            BasicTransition.Forward -> ForwardEnterTransition(container)
            BasicTransition.Backward -> BackwardEnterTransition(container)
            BasicTransition.Next -> NextEnterTransition(container)
            BasicTransition.Previous -> PreviousEnterTransition(container)
            else -> null
        }?.withDuration(duration)
    }

    /**
     * Called when [ViewBinderStackAdapter] removes [view] from [container]
     *
     * @param container in which [view] added
     * @param view to be animated and removed
     * @param transition specification of animation
     * @param duration default time, set for animations in [ViewBinderStackAdapter]
     */
    open fun exitTransition(
        container: ViewGroup, view: View, transition: Any, duration: Long
    ): Transition? {
        return when (transition) {
            BasicTransition.Fade -> FadeOutTransition()
            BasicTransition.Forward -> ForwardExitTransition(container)
            BasicTransition.Backward -> BackwardExitTransition(container)
            BasicTransition.Next -> NextExitTransition(container)
            BasicTransition.Previous -> PreviousExitTransition(container)
            else -> null
        }?.withDuration(duration)
    }


}