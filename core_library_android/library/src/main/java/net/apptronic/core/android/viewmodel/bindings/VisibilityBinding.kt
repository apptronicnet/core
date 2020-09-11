package net.apptronic.core.android.viewmodel.bindings

import android.view.View
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.transitions.*
import net.apptronic.core.android.viewmodel.transitions.transformation.ViewSwitchTransitionBuilder
import net.apptronic.core.android.viewmodel.transitions.transformation.transition
import net.apptronic.core.base.observable.distinctUntilChanged
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.mvvm.viewmodel.ViewModel

fun BindingContainer.bindVisibleInvisible(
    view: View, target: Entity<Boolean>,
    enter: (View) -> Transition<View> = { EmptyViewTransition() },
    exit: (View) -> Transition<View> = { EmptyViewTransition() }
) {
    +VisibilityBinding(view, target, View.INVISIBLE, enter, exit)
}

fun BindingContainer.bindVisibleGone(
    view: View, target: Entity<Boolean>,
    enter: (View) -> Transition<View> = { EmptyViewTransition() },
    exit: (View) -> Transition<View> = { EmptyViewTransition() }
) {
    +VisibilityBinding(view, target, View.GONE, enter, exit)
}

fun BindingContainer.bindVisibleInvisible(
    view: View, target: Entity<Boolean>, builder: ViewSwitchTransitionBuilder.() -> Unit,
    duration: Long = view.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
) {
    +VisibilitySwitchBuilderBinding(view, target, View.INVISIBLE, builder, duration)
}

fun BindingContainer.bindVisibleGone(
    view: View, target: Entity<Boolean>, builder: ViewSwitchTransitionBuilder.() -> Unit,
    duration: Long = view.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
) {
    +VisibilitySwitchBuilderBinding(view, target, View.GONE, builder, duration)
}

fun BindingContainer.bindVisibleInvisible(
    view: View, target: Entity<Boolean>,
    transitionSpec: Any, transitionBuilder: TransitionBuilder,
    duration: Long = view.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
) {
    +VisibilitySwitchBinding(
        view,
        target,
        View.INVISIBLE,
        transitionSpec,
        transitionBuilder,
        duration
    )
}

fun BindingContainer.bindVisibleGone(
    view: View, target: Entity<Boolean>,
    transitionSpec: Any, transitionBuilder: TransitionBuilder,
    duration: Long = view.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
) {
    +VisibilitySwitchBinding(view, target, View.GONE, transitionSpec, transitionBuilder, duration)
}

private class VisibilityBinding(
    private val view: View,
    private val target: Entity<Boolean>,
    private val invisibleState: Int,
    private val enterBuilder: (View) -> Transition<View> = { EmptyViewTransition() },
    private val exitBuilder: (View) -> Transition<View> = { EmptyViewTransition() },
) : Binding() {

    override fun onBind(viewModel: ViewModel, viewBinder: ViewBinder<*>) {
        target.distinctUntilChanged().subscribe { isVisible ->
            if (viewModel.isVisible()) {
                if (isVisible) {
                    enterBuilder(view).doOnStart {
                        view.visibility = View.VISIBLE
                    }.launch(view)
                } else {
                    exitBuilder(view).doOnCompleteOrCancel {
                        view.visibility = invisibleState
                    }.launch(view)
                }
            } else {
                view.visibility = if (isVisible) View.VISIBLE else invisibleState
            }
        }
    }

}

private class VisibilitySwitchBuilderBinding(
    private val view: View,
    private val target: Entity<Boolean>,
    private val invisibleState: Int,
    private val builder: ViewSwitchTransitionBuilder.() -> Unit,
    private val duration: Long
) : Binding() {

    override fun onBind(viewModel: ViewModel, viewBinder: ViewBinder<*>) {
        target.distinctUntilChanged().subscribe { isVisible ->
            if (viewModel.isVisible()) {
                if (isVisible) {
                    val viewSwitch = ViewSwitch(view, null, view.getTransitionParent(), false)
                    val transition = viewSwitch.transition(builder).withDuration(duration)
                    transition.doOnStart {
                        view.visibility = View.VISIBLE
                    }
                    transition.launch(viewSwitch)
                } else {
                    val viewSwitch = ViewSwitch(null, view, view.getTransitionParent(), false)
                    val transition = viewSwitch.transition(builder).withDuration(duration)
                    transition.doOnCompleteOrCancel {
                        view.visibility = invisibleState
                    }
                    transition.launch(viewSwitch)
                }
            } else {
                view.visibility = if (isVisible) View.VISIBLE else invisibleState
            }
        }
    }

}

private class VisibilitySwitchBinding(
    private val view: View,
    private val target: Entity<Boolean>,
    private val invisibleState: Int,
    private val transitionSpec: Any,
    private val transitionBuilder: TransitionBuilder,
    private val duration: Long
) : Binding() {

    override fun onBind(viewModel: ViewModel, viewBinder: ViewBinder<*>) {
        target.distinctUntilChanged().subscribe { isVisible ->
            if (viewModel.isVisible()) {
                if (isVisible) {
                    val viewSwitch = ViewSwitch(view, null, view.getTransitionParent(), false)
                    val transition =
                        transitionBuilder.getViewSwitchTransition(
                            viewSwitch,
                            transitionSpec,
                            duration
                        )
                    transition.doOnStart {
                        view.visibility = View.VISIBLE
                    }
                    transition.launch(viewSwitch)
                } else {
                    val viewSwitch = ViewSwitch(null, view, view.getTransitionParent(), false)
                    val transition =
                        transitionBuilder.getViewSwitchTransition(
                            viewSwitch,
                            transitionSpec,
                            duration
                        )
                    transition.doOnCompleteOrCancel {
                        view.visibility = invisibleState
                    }
                    transition.launch(viewSwitch)

                }
            } else {
                view.visibility = if (isVisible) android.view.View.VISIBLE else invisibleState
            }
        }
    }

}