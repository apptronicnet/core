package net.apptronic.core.android.viewmodel.navigation

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.style.list.ListItemStyleAdapter
import net.apptronic.core.android.viewmodel.style.list.emptyStyleAdapter
import net.apptronic.core.android.viewmodel.view.ViewContainerDelegate
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ItemStateNavigator
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

class ViewBinderListAdapter(
    private val viewBinderFactory: ViewBinderFactory = ViewBinderFactory(),
    private val styleAdapter: ListItemStyleAdapter = emptyStyleAdapter(),
    private val itemStateNavigator: ItemStateNavigator
) : ViewModelListAdapter {

    private val listeners = mutableListOf<UpdateListener>()
    private var items: List<ViewModel> = emptyList()

    interface UpdateListener {
        fun onDataChanged(items: List<ViewModel>, changeInfo: Any?)
    }

    fun addListener(listener: UpdateListener) {
        listeners.add(listener)
        listener.onDataChanged(items, null)
    }

    fun getSize(): Int {
        return items.size
    }

    fun getViewModelAt(index: Int): ViewModel {
        return items[index]
    }

    fun getId(index: Int): Long {
        return items[index].componentId
    }

    fun contains(viewModel: ViewModel): Boolean {
        return items.contains(viewModel)
    }

    override fun onDataChanged(items: List<ViewModel>, changeInfo: Any?) {
        this.items = items
        listeners.forEach {
            it.onDataChanged(items, changeInfo)
        }
    }

    fun indexOf(binder: ViewBinder<*>): Int {
        return items.indexOf(binder.getViewModel())
    }

    private val boundViews = mutableMapOf<Long, ViewBinder<*>>()

    fun bindings(setup: ViewBinderFactory.() -> Unit) {
        setup.invoke(viewBinderFactory)
    }

    fun createView(typeId: Int, container: ViewGroup): View {
        val binder = viewBinderFactory.getBinder(typeId)
        val delegate = binder.getViewDelegate<ViewContainerDelegate<*>>()
        return delegate.performCreateDetachedView(container.context, binder, null, container)
    }

    fun createView(viewModel: ViewModel, container: ViewGroup): View {
        val binder = viewBinderFactory.getBinder(viewModel)
        val delegate = binder.getViewDelegate<ViewContainerDelegate<*>>()
        return delegate.performCreateDetachedView(container.context, binder, null, container)
    }

    fun getViewType(position: Int): Int {
        return viewBinderFactory.getType(items[position])
    }

    fun getViewType(viewModel: ViewModel): Int {
        return viewBinderFactory.getType(viewModel)
    }

    fun bindView(position: Int, view: View): ViewBinder<*> {
        val viewModel = items[position]
        return bindView(viewModel, position, view)
    }

    fun bindView(viewModel: ViewModel, view: View): ViewBinder<*> {
        val position = items.indexOf(viewModel)
        return bindView(viewModel, position, view)
    }

    private fun bindView(viewModel: ViewModel, position: Int, view: View): ViewBinder<*> {
        val oldBinder: ViewBinder<*>? = boundViews[viewModel.componentId]
        styleAdapter.applyViewStyle(view, position, items)
        return if (oldBinder != null) {
            if (oldBinder.getViewModel() != viewModel) {
                // TODO should not happen in RecyclerView
                // TODO need to check
                setFocused(oldBinder, false)
                setVisible(oldBinder, false)
                // END TODO
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
        itemStateNavigator.setBound(viewModel, true)
        binder.performViewBinding(viewModel, view)
        return binder
    }

    fun unbindView(viewBinder: ViewBinder<*>) {
        val viewModel = viewBinder.getViewModel()
        if (boundViews.containsKey(viewModel.componentId)) {
            itemStateNavigator.setBound(viewModel, false)
            boundViews.remove(viewModel.componentId)
        }
    }

    fun setVisible(viewBinder: ViewBinder<*>, isVisible: Boolean) {
        itemStateNavigator.setVisible(viewBinder.getViewModel(), isVisible)
    }

    fun setFocused(viewBinder: ViewBinder<*>, isFocused: Boolean) {
        itemStateNavigator.setFocused(viewBinder.getViewModel(), isFocused)
    }

}