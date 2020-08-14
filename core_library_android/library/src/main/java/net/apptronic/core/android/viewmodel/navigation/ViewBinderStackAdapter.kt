package net.apptronic.core.android.viewmodel.navigation

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.transitions.TransitionBuilder
import net.apptronic.core.android.viewmodel.transitions.ViewSwitch
import net.apptronic.core.android.viewmodel.view.ViewContainerDelegate
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelStackAdapter
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigator
import net.apptronic.core.mvvm.viewmodel.navigation.TransitionInfo

/**
 * Adapter for [StackNavigator]
 *
 * @param container in which [View] should be added
 * @param viewBinderFactory to create [ViewBinder] for [ViewModel]
 * @param transitionBuilder for creating animations
 */
open class ViewBinderStackAdapter(
    private val container: ViewGroup,
    private val viewBinderFactory: ViewBinderFactory = ViewBinderFactory(),
    private val transitionBuilder: TransitionBuilder = TransitionBuilder(),
    private val defaultAnimationTime: Long =
        container.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
) : ViewModelStackAdapter() {

    fun bindings(setup: ViewBinderFactory.() -> Unit) {
        setup.invoke(viewBinderFactory)
    }

    private var currentBinder: ViewBinder<*>? = null

    override fun onInvalidate(newModel: ViewModel?, transitionInfo: TransitionInfo) {
        val newBinder =
            if (newModel != null) viewBinderFactory.getBinder(newModel) else null
        if (newBinder != null && newModel != null) {
            val delegate = newBinder.getViewDelegate<ViewContainerDelegate<*>>()
            val view =
                delegate.performCreateView(newModel, newBinder, container.context, null, container)
            newBinder.performViewBinding(newModel, view)
        }
        setView(newBinder, transitionInfo.isNewOnFront, transitionInfo.spec)
    }

    private fun setView(newBinder: ViewBinder<*>?, isNewOnFront: Boolean, transitionSpec: Any?) {
        val oldBinder = currentBinder
        currentBinder = newBinder
        if (oldBinder != null && newBinder != null) {
            onReplace(
                newBinder.getViewModel(),
                newBinder,
                container,
                oldBinder.getView(),
                newBinder.getView(),
                isNewOnFront,
                transitionSpec
            )
        } else if (newBinder != null) {
            onAdd(
                newBinder.getViewModel(),
                newBinder, container, newBinder.getView(), transitionSpec
            )
        } else if (oldBinder != null) {
            onRemove(
                oldBinder.getViewModel(),
                oldBinder, container, oldBinder.getView(), transitionSpec
            )
        }
    }

    open fun onAdd(
        viewModel: ViewModel,
        viewBinder: ViewBinder<*>,
        container: ViewGroup,
        newView: View,
        transitionSpec: Any?
    ) {
        val viewSwitch = ViewSwitch(
            entering = newView,
            exiting = null,
            container = container,
            isNewOnFront = true
        )
        val transition = transitionBuilder.getViewSwitchTransition(
            viewSwitch, transitionSpec, defaultAnimationTime
        )
        addViewToContainer(viewModel, viewBinder, container, newView, true)
        newView.visibility = View.INVISIBLE
        transition.doOnStart {
            newView.visibility = View.VISIBLE
        }.launch(viewSwitch)
    }

    open fun onReplace(
        viewModel: ViewModel,
        viewBinder: ViewBinder<*>,
        container: ViewGroup,
        oldView: View,
        newView: View,
        isNewOnFront: Boolean,
        transitionSpec: Any?
    ) {
        val viewSwitch = ViewSwitch(
            entering = newView,
            exiting = oldView,
            container = container,
            isNewOnFront = isNewOnFront
        )
        val transition = transitionBuilder.getViewSwitchTransition(
            viewSwitch, transitionSpec, defaultAnimationTime
        )
        addViewToContainer(viewModel, viewBinder, container, newView, isNewOnFront)
        newView.visibility = View.INVISIBLE
        transition.doOnStart {
            newView.visibility = View.VISIBLE
        }.doOnCompleteOrCancel {
            val delegate = viewBinder.getViewDelegate<ViewContainerDelegate<*>>()
            delegate.performDetachView(viewModel, viewBinder, oldView, container)
        }.launch(viewSwitch)
    }

    open fun onRemove(
        viewModel: ViewModel,
        viewBinder: ViewBinder<*>,
        container: ViewGroup,
        oldView: View,
        transitionSpec: Any?
    ) {
        val viewSwitch = ViewSwitch(
            entering = null,
            exiting = oldView,
            container = container,
            isNewOnFront = false
        )
        val transition = transitionBuilder.getViewSwitchTransition(
            viewSwitch, transitionSpec, defaultAnimationTime
        )
        transition.doOnCompleteOrCancel {
            container.removeView(oldView)
        }.launch(viewSwitch)
    }

    private fun addViewToContainer(
        viewModel: ViewModel,
        viewBinder: ViewBinder<*>,
        container: ViewGroup,
        child: View,
        toFront: Boolean
    ) {
        val delegate = viewBinder.getViewDelegate<ViewContainerDelegate<*>>()
        val position = if (toFront) {
            container.childCount
        } else {
            0
        }
        delegate.performAttachView(viewModel, child, container, position)
    }

}