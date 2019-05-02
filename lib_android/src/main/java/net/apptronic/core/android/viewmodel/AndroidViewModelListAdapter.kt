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

    fun bindView(viewModel: ViewModel, view: View): AndroidView<*> {
        boundViews.filter {
            it.getView() == view
        }.forEach {
            unbindView(it)
        }
        val androidView = androidViewFactory.getAndroidView(viewModel)
        setBound(viewModel, true)
        androidView.bindView(view, viewModel)
        setVisible(viewModel, true)
        setFocused(viewModel, true)
        boundViews.add(androidView)
        return androidView
    }

    fun unbindView(androidView: AndroidView<*>) {
        if (boundViews.remove(androidView)) {
            setFocused(androidView.getViewModel(), false)
            setVisible(androidView.getViewModel(), false)
            setBound(androidView.getViewModel(), false)
        }
    }

}