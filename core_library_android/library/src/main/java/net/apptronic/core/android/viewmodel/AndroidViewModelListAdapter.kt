package net.apptronic.core.android.viewmodel

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

class AndroidViewModelListAdapter(
    private val androidViewFactory: AndroidViewFactory = AndroidViewFactory()
) : ViewModelListAdapter() {

    private val boundViews = mutableMapOf<Long, AndroidView<*>>()

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
        view: View
    ): AndroidView<*> {
        val oldBoundView: AndroidView<*>? = boundViews[viewModel.getId()]
        return if (oldBoundView != null) {
            if (oldBoundView.getViewModel() != viewModel) {
                unbindView(oldBoundView)
                performNewBinding(viewModel, view)
            } else {
                oldBoundView
            }
        } else {
            performNewBinding(viewModel, view)
        }.also {
            boundViews[viewModel.getId()] = it
        }
    }

    private fun performNewBinding(viewModel: ViewModel, view: View): AndroidView<*> {
        val androidView = androidViewFactory.getAndroidView(viewModel)
        setBound(viewModel, true)
        androidView.bindView(view, viewModel)
        setVisible(viewModel, true)
        setFocused(viewModel, true)
        return androidView
    }

    fun unbindView(androidView: AndroidView<*>) {
        val viewModel = androidView.getViewModel()
        if (boundViews.containsKey(viewModel.getId())) {
            setFocused(androidView.getViewModel(), false)
            setVisible(androidView.getViewModel(), false)
            setBound(androidView.getViewModel(), false)
            boundViews.remove(viewModel.getId())
        }
    }

}