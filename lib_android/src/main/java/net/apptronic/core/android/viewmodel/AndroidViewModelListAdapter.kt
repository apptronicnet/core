package net.apptronic.core.android.viewmodel

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

class AndroidViewModelListAdapter(
    viewController: ViewController,
    private val viewBindingFactory: ViewBindingFactory = ViewBindingFactory()
) : ViewModelListAdapter(viewController) {

    fun bindings(setup: ViewBindingFactory.() -> Unit) {
        setup.invoke(viewBindingFactory)
    }

    fun createView(typeId: Int, container: ViewGroup): View {
        return viewBindingFactory.getAndroidView(typeId).onCreateView(container)
    }

    fun createView(viewModel: ViewModel, container: ViewGroup): View {
        return viewBindingFactory.getAndroidView(viewModel).onCreateView(container)
    }

    fun getViewType(viewModel: ViewModel): Int {
        return viewBindingFactory.getType(viewModel)
    }

    fun bindView(viewModel: ViewModel, view: View) {
        val androidView = viewBindingFactory.getAndroidView(viewModel)
        androidView.requestBinding(view, viewModel)
    }

}