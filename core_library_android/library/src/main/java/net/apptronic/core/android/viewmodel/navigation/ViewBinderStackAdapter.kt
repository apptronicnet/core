package net.apptronic.core.android.viewmodel.navigation

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.transitions.TransitionBuilder
import net.apptronic.core.android.viewmodel.transitions.ViewSwitch
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelStackAdapter
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigator
import kotlin.math.max

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

    override fun onInvalidate(newModel: ViewModel?, isNewOnFront: Boolean, transitionInfo: Any?) {
        val newBinder =
            if (newModel != null) viewBinderFactory.getBinder(newModel) else null
        if (newBinder != null && newModel != null) {
            val view = newBinder.onCreateView(container)
            newBinder.performViewBinding(view, newModel)
        }
        setView(newBinder, isNewOnFront, transitionInfo)
    }

    private fun setView(newBinder: ViewBinder<*>?, isNewOnFront: Boolean, transitionInfo: Any?) {
        val oldAndroidView = currentBinder
        currentBinder = newBinder
        if (oldAndroidView != null && newBinder != null) {
            onReplace(
                container,
                oldAndroidView.getView(),
                newBinder.getView(),
                isNewOnFront,
                transitionInfo
            )
        } else if (newBinder != null) {
            onAdd(container, newBinder.getView(), transitionInfo)
        } else if (oldAndroidView != null) {
            onRemove(container, oldAndroidView.getView(), transitionInfo)
        }
    }

    open fun onAdd(
        container: ViewGroup,
        newView: View,
        transitionInfo: Any?
    ) {
        val viewSwitch = ViewSwitch(
            entering = newView,
            exiting = null,
            container = container,
            isNewOnFront = true
        )
        val transition = transitionBuilder.getViewSwitchTransition(
            viewSwitch, transitionInfo, defaultAnimationTime
        )
        container.addView(newView, true)
        newView.visibility = View.INVISIBLE
        transition.doOnStart {
            newView.visibility = View.VISIBLE
        }.launch(viewSwitch)
    }

    open fun onReplace(
        container: ViewGroup,
        oldView: View,
        newView: View,
        isNewOnFront: Boolean,
        transitionInfo: Any?
    ) {
        val viewSwitch = ViewSwitch(
            entering = newView,
            exiting = oldView,
            container = container,
            isNewOnFront = isNewOnFront
        )
        val transition = transitionBuilder.getViewSwitchTransition(
            viewSwitch, transitionInfo, defaultAnimationTime
        )
        container.addView(newView, isNewOnFront)
        newView.visibility = View.INVISIBLE
        transition.doOnStart {
            newView.visibility = View.VISIBLE
        }.doOnCompleteOrCancel {
            container.removeView(oldView)
        }.launch(viewSwitch)
    }

    open fun onRemove(container: ViewGroup, oldView: View, transitionInfo: Any?) {
        val viewSwitch = ViewSwitch(
            entering = null,
            exiting = oldView,
            container = container,
            isNewOnFront = false
        )
        val transition = transitionBuilder.getViewSwitchTransition(
            viewSwitch, transitionInfo, defaultAnimationTime
        )
        transition.doOnCompleteOrCancel {
            container.removeView(oldView)
        }.launch(viewSwitch)
    }

    private fun ViewGroup.addView(child: View, toFront: Boolean) {
        if (toFront) {
            addView(child)
        } else {
            addView(child, max(childCount - 1, 0))
        }
    }

}