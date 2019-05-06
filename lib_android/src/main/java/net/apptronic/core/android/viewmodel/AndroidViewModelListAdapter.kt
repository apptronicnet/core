package net.apptronic.core.android.viewmodel

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

class AndroidViewModelListAdapter(
    private val androidViewFactory: AndroidViewFactory = AndroidViewFactory()
) : ViewModelListAdapter() {

    private val boundViews = mutableListOf<AndroidView<*>>()

    fun bindings(setup: AndroidViewFactory.() -> Unit) {
        setup.invoke(androidViewFactory)
    }

    fun createView(typeId: Int, container: ViewGroup): View {
        return androidViewFactory.getAndroidView(typeId).onCreateView(container)
    }

    fun createView(viewModel: ViewModel, container: ViewGroup): View {
        return androidViewFactory.getAndroidView(viewModel).onCreateView(container)
    }

    fun getViewType(position: Int): Int {
        return androidViewFactory.getType(getItemAt(position))
    }

    fun getViewType(viewModel: ViewModel): Int {
        return androidViewFactory.getType(viewModel)
    }

    fun bindView(
        viewModel: ViewModel,
        view: View,
        oldBoundView: AndroidView<*>? = null
    ): AndroidView<*> {
        val androidView = androidViewFactory.getAndroidView(viewModel)
        if (oldBoundView != null) {
            if (oldBoundView != androidView) {
                unbindView(oldBoundView)
                performNewBinding(viewModel, view, androidView)
            }
        } else {
            performNewBinding(viewModel, view, androidView)
        }
        return androidView
    }

    private fun performNewBinding(viewModel: ViewModel, view: View, androidView: AndroidView<*>) {
        setBound(viewModel, true)
        androidView.bindView(view, viewModel)
        setVisible(viewModel, true)
        setFocused(viewModel, true)
    }

    fun unbindView(androidView: AndroidView<*>) {
        setFocused(androidView.getViewModel(), false)
        setVisible(androidView.getViewModel(), false)
        setBound(androidView.getViewModel(), false)
    }

}