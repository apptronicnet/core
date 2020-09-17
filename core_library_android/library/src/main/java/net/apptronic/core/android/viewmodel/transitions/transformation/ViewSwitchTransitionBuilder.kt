package net.apptronic.core.android.viewmodel.transitions.transformation

import android.view.View
import net.apptronic.core.android.viewmodel.transitions.Transition
import net.apptronic.core.android.viewmodel.transitions.ViewSwitch
import net.apptronic.core.android.viewmodel.transitions.ViewSwitchTransition

@Deprecated("Replaced by net.apptronic.core.android.anim.*")
class ViewSwitchTransitionBuilder internal constructor(
    private val viewSwitch: ViewSwitch
) {

    private var enter: Transition<View>? = null
    private var exit: Transition<View>? = null

    val entering = viewSwitch.entering
    val exiting = viewSwitch.exiting
    val container = viewSwitch.container
    val isNewOnFront = viewSwitch.isNewOnFront

    fun enter(builder: TransformationTransitionBuilder.() -> Unit) {
        enter = entering?.let {
            transformationTransition(it, container, builder)
        }
    }

    fun exit(builder: TransformationTransitionBuilder.() -> Unit) {
        exit = exiting?.let {
            transformationTransition(it, container, builder)
        }
    }

    internal fun build(): Transition<ViewSwitch> {
        return ViewSwitchTransition(enter, exit)
    }

}

@Deprecated("Replaced by net.apptronic.core.android.anim.*")
fun ViewSwitch.transition(
    builder: ViewSwitchTransitionBuilder.() -> Unit
): Transition<ViewSwitch> {
    return ViewSwitchTransitionBuilder(this)
        .apply(builder)
        .build()
}