package net.apptronic.core.android.viewmodel.navigation

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelStackAdapter
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigator

/**
 * Adapter for [StackNavigator]
 *
 * @param container in which [View] should be added
 * @param viewBinderFactory to create [ViewBinder] for [ViewModel]
 * @param stackAnimator for creating animations
 */
open class ViewBinderStackAdapter(
    private val container: ViewGroup,
    private val viewBinderFactory: ViewBinderFactory = ViewBinderFactory(),
    private val stackAnimator: StackAnimator = StackAnimator(),
    private val defaultAnimationTime: Long =
        container.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
) : ViewModelStackAdapter() {

    fun bindings(setup: ViewBinderFactory.() -> Unit) {
        setup.invoke(viewBinderFactory)
    }

    private var currentBinder: ViewBinder<*>? = null

    override fun onInvalidate(oldModel: ViewModel?, newModel: ViewModel?, transitionInfo: Any?) {
        val newAndroidView =
            if (newModel != null) viewBinderFactory.getBinder(newModel) else null
        if (newAndroidView != null && newModel != null) {
            val view = newAndroidView.onCreateView(container)
            newAndroidView.bindView(view, newModel)
        }
        setView(newAndroidView, transitionInfo)
    }

    private fun setView(newBinder: ViewBinder<*>?, transitionInfo: Any?) {
        val oldAndroidView = currentBinder
        currentBinder = newBinder
        if (oldAndroidView != null && newBinder != null) {
            onReplace(container, oldAndroidView.getView(), newBinder.getView(), transitionInfo)
        } else if (newBinder != null) {
            onAdd(container, newBinder.getView(), transitionInfo)
        } else if (oldAndroidView != null) {
            onRemove(container, oldAndroidView.getView(), transitionInfo)
        }
    }

    open fun onAdd(container: ViewGroup, newView: View, transitionInfo: Any?) {
        if (transitionInfo != null) {
            stackAnimator.applyEnterTransition(
                container,
                newView,
                transitionInfo,
                defaultAnimationTime
            )
        } else {
            container.addView(newView)
        }
    }

    open fun onReplace(container: ViewGroup, oldView: View, newView: View, transitionInfo: Any?) {
        if (transitionInfo != null) {
            stackAnimator.applyEnterTransition(
                container,
                newView,
                transitionInfo,
                defaultAnimationTime
            )
            stackAnimator.applyExitTransition(
                container,
                oldView,
                transitionInfo,
                defaultAnimationTime
            )
        } else {
            container.addView(newView)
            container.removeView(oldView)
        }
    }

    open fun onRemove(container: ViewGroup, oldView: View, transitionInfo: Any?) {
        if (transitionInfo != null) {
            stackAnimator.applyExitTransition(
                container,
                oldView,
                transitionInfo,
                defaultAnimationTime
            )
        } else {
            container.removeView(oldView)
        }
    }

}