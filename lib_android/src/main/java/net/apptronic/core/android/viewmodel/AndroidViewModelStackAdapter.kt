package net.apptronic.core.android.viewmodel

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelAdapter

open class AndroidViewModelStackAdapter(
    private val container: ViewGroup,
    private val viewBindingFactory: ViewBindingFactory = ViewBindingFactory()
) : ViewModelAdapter() {

    private var animationTime =
        container.resources.getInteger(android.R.integer.config_mediumAnimTime)

    fun bindings(setup: ViewBindingFactory.() -> Unit) {
        setup.invoke(viewBindingFactory)
    }

    private var currentView: AndroidView<*>? = null

    override fun onInvalidate(oldModel: ViewModel?, newModel: ViewModel?, transitionInfo: Any?) {
        val newAndroidView =
            if (newModel != null) viewBindingFactory.getAndroidView(newModel) else null
        if (newAndroidView != null && newModel != null) {
            val view = newAndroidView.onCreateView(container)
            newAndroidView.bindView(view, newModel)
        }
        setView(newAndroidView, transitionInfo)
    }

    private fun setView(newAndroidView: AndroidView<*>?, transitionInfo: Any?) {
        val oldAndroidView = currentView
        currentView = newAndroidView
        if (oldAndroidView != null && newAndroidView != null) {
            onReplace(container, oldAndroidView.getView(), newAndroidView.getView(), transitionInfo)
        } else if (newAndroidView != null) {
            onAdd(container, newAndroidView.getView(), transitionInfo)
        } else if (oldAndroidView != null) {
            onRemove(container, oldAndroidView.getView(), transitionInfo)
        }
    }

    open fun onAdd(container: ViewGroup, newView: View, transitionInfo: Any?) {
        if (transitionInfo is BasicTransition) {
            applyEnterTransition(container, newView, transitionInfo, animationTime)
        } else {
            container.addView(newView)
        }
    }

    open fun onReplace(container: ViewGroup, oldView: View, newView: View, transitionInfo: Any?) {
        if (transitionInfo is BasicTransition) {
            applyExitTransition(container, oldView, transitionInfo, animationTime)
            applyEnterTransition(container, newView, transitionInfo, animationTime)
        } else {
            container.removeView(oldView)
            container.addView(newView)
        }
    }

    open fun onRemove(container: ViewGroup, oldView: View, transitionInfo: Any?) {
        if (transitionInfo is BasicTransition) {
            applyExitTransition(container, oldView, transitionInfo, animationTime)
        } else {
            container.removeView(oldView)
        }
    }


}