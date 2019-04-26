package net.apptronic.core.android.viewmodel

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

class AndroidViewModelListAdapter(
    private val viewBindingFactory: ViewBindingFactory = ViewBindingFactory()
) : ViewModelListAdapter() {

    private val boundViews = mutableListOf<AndroidView<*>>()

    fun bindings(setup: ViewBindingFactory.() -> Unit) {
        setup.invoke(viewBindingFactory)
    }

    fun createView(typeId: Int, container: ViewGroup): View {
        return viewBindingFactory.getAndroidView(typeId).onCreateView(container)
    }

    fun createView(viewModel: ViewModel, container: ViewGroup): View {
        return viewBindingFactory.getAndroidView(viewModel).onCreateView(container)
    }

    fun getViewType(position: Int): Int {
        return viewBindingFactory.getType(getItemAt(position))
    }

    fun getViewType(viewModel: ViewModel): Int {
        return viewBindingFactory.getType(viewModel)
    }

    fun bindView(viewModel: ViewModel, view: View): AndroidView<*> {
        boundViews.filter {
            it.getView() == view
        }.forEach {
            unbindView(it)
        }
        val androidView = viewBindingFactory.getAndroidView(viewModel)
        setBound(viewModel, true)
        androidView.bindView(view, viewModel)
        boundViews.add(androidView)
        return androidView
    }

    fun unbindView(androidView: AndroidView<*>) {
        if (boundViews.remove(androidView)) {
            setBound(androidView.getViewModel(), false)
        }
    }

}