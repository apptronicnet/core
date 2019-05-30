package net.apptronic.core.android.viewmodel

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelStackAdapter

open class AndroidViewModelStackAdapter(
        private val container: ViewGroup,
        private val androidViewFactory: AndroidViewFactory = AndroidViewFactory(),
        private val stackAnimator: StackAnimator = StackAnimator(),
        private val defaultAnimationTime: Long = container.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
) : ViewModelStackAdapter() {

    fun bindings(setup: AndroidViewFactory.() -> Unit) {
        setup.invoke(androidViewFactory)
    }

    private var currentView: AndroidView<*>? = null

    override fun onInvalidate(oldModel: ViewModel?, newModel: ViewModel?, transitionInfo: Any?) {
        val newAndroidView =
                if (newModel != null) androidViewFactory.getAndroidView(newModel) else null
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
        if (transitionInfo != null) {
            stackAnimator.applyEnterTransition(container, newView, transitionInfo, defaultAnimationTime)
        } else {
            container.addView(newView)
        }
    }

    open fun onReplace(container: ViewGroup, oldView: View, newView: View, transitionInfo: Any?) {
        if (transitionInfo != null) {
            stackAnimator.applyEnterTransition(container, newView, transitionInfo, defaultAnimationTime)
            stackAnimator.applyExitTransition(container, oldView, transitionInfo, defaultAnimationTime)
        } else {
            container.addView(newView)
            container.removeView(oldView)
        }
    }

    open fun onRemove(container: ViewGroup, oldView: View, transitionInfo: Any?) {
        if (transitionInfo != null) {
            stackAnimator.applyExitTransition(container, oldView, transitionInfo, defaultAnimationTime)
        } else {
            container.removeView(oldView)
        }
    }

}