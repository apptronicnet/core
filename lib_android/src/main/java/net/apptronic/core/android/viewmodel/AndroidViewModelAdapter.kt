package net.apptronic.core.android.viewmodel

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelAdapter

open class AndroidViewModelAdapter(
    private val container: ViewGroup
) : ViewModelAdapter() {

    private var viewFactory = AndroidViewFactory()

    fun setBindingFactory(viewFactory: AndroidViewFactory) {
        this.viewFactory = viewFactory
    }

    fun bindings(setup: AndroidViewFactory.() -> Unit) {
        setup.invoke(viewFactory)
    }

    private var currentView: AndroidView<*>? = null

    override fun onInvalidate(oldModel: ViewModel?, newModel: ViewModel?, transitionInfo: Any?) {
        val newAndroidView = if (newModel != null) viewFactory.build(newModel) else null
        setView(newAndroidView, transitionInfo)
    }

    fun setView(newView: AndroidView<*>?, transitionInfo: Any?) {
        val oldView = currentView
        currentView = newView
        if (oldView != null && newView != null) {
            onReplace(container, oldView.getView(), newView.getView(), transitionInfo)
        } else if (newView != null) {
            onAdd(container, newView.getView(), transitionInfo)
        } else if (oldView != null) {
            onRemove(container, oldView.getView(), transitionInfo)
        }
    }

    open fun onAdd(container: ViewGroup, newView: View, transitionInfo: Any?) {
        container.addView(newView)
    }

    open fun onReplace(container: ViewGroup, oldView: View, newView: View, transitionInfo: Any?) {
        container.removeView(oldView)
        container.addView(newView)
    }

    open fun onRemove(container: ViewGroup, oldView: View, transitionInfo: Any?) {
        container.removeView(oldView)
    }


}