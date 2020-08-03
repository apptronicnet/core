package net.apptronic.core.android.viewmodel.navigation

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.style.list.ListItemStyleAdapter
import net.apptronic.core.android.viewmodel.style.list.emptyStyleAdapter
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

class ViewBinderListAdapter(
    private val viewBinderFactory: ViewBinderFactory = ViewBinderFactory(),
    private val styleAdapter: ListItemStyleAdapter = emptyStyleAdapter()
) : ViewModelListAdapter() {

    private val boundViews = mutableMapOf<Long, ViewBinder<*>>()

    fun bindings(setup: ViewBinderFactory.() -> Unit) {
        setup.invoke(viewBinderFactory)
    }

    fun createView(typeId: Int, container: ViewGroup): View {
        return viewBinderFactory.getBinder(typeId)
            .performCreateView(container.context, null, container)
    }

    fun createView(viewModel: ViewModel, container: ViewGroup): View {
        return viewBinderFactory.getBinder(viewModel)
            .performCreateView(container.context, null, container)
    }

    fun getViewType(position: Int): Int {
        return viewBinderFactory.getType(getItemAt(position))
    }

    fun getViewType(viewModel: ViewModel): Int {
        return viewBinderFactory.getType(viewModel)
    }

    fun bindView(
        viewModel: ViewModel,
        position: Int,
        view: View
    ): ViewBinder<*> {
        val oldBinder: ViewBinder<*>? = boundViews[viewModel.componentId]
        styleAdapter.applyViewStyle(view, position, getItems())
        return if (oldBinder != null) {
            if (oldBinder.getViewModel() != viewModel) {
                unbindView(oldBinder)
                performNewBinding(viewModel, view)
            } else {
                oldBinder
            }
        } else {
            performNewBinding(viewModel, view)
        }.also {
            boundViews[viewModel.componentId] = it
        }
    }

    private fun performNewBinding(viewModel: ViewModel, view: View): ViewBinder<*> {
        val binder = viewBinderFactory.getBinder(viewModel)
        setBound(viewModel, true)
        binder.performViewBinding(view, viewModel)
        setVisible(viewModel, true)
        setFocused(viewModel, true)
        return binder
    }

    fun unbindView(viewBinder: ViewBinder<*>) {
        val viewModel = viewBinder.getViewModel()
        if (boundViews.containsKey(viewModel.componentId)) {
            setFocused(viewModel, false)
            setVisible(viewModel, false)
            setBound(viewModel, false)
            boundViews.remove(viewModel.componentId)
        }
    }

}