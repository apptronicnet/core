package net.apptronic.core.android.viewmodel.navigation

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.transitions.TransitionBuilder
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelStackAdapter
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigator

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
        val transition = transitionBuilder.getEnterTransition(
            container, newView, transitionInfo, defaultAnimationTime
        )
        transition.doOnStart {
            container.addView(newView, transition.isFrontTransition)
        }.start(newView)
    }

    open fun onReplace(container: ViewGroup, oldView: View, newView: View, transitionInfo: Any?) {
        onRemove(container, oldView, transitionInfo)
        onAdd(container, newView, transitionInfo)
    }

    open fun onRemove(container: ViewGroup, oldView: View, transitionInfo: Any?) {
        val transition = transitionBuilder.getExitTransition(
            container, oldView, transitionInfo, defaultAnimationTime
        )
        transition.doOnComplete {
            container.removeView(oldView)
        }.start(oldView)
    }

    private fun ViewGroup.addView(child: View, toFront: Boolean) {
        if (toFront) {
            addView(child)
        } else {
            addView(child, 0)
        }
    }

}