package net.apptronic.core.android.viewmodel.navigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderAdapter
import net.apptronic.core.android.viewmodel.style.list.ViewStyleAdapter
import net.apptronic.core.android.viewmodel.view.DefaultViewContainerViewAdapter
import net.apptronic.core.android.viewmodel.view.ViewContainerViewAdapter
import net.apptronic.core.base.collections.simpleLazyListOf
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.navigation.ViewModelItem
import net.apptronic.core.viewmodel.navigation.adapters.ViewModelListAdapter

class ViewBinderListAdapter(
    private val container: ViewGroup,
    private val viewBinderAdapter: ViewBinderAdapter,
    private val styleAdapter: ViewStyleAdapter
) : ViewModelListAdapter<Any?> {

    private val layoutInflater = LayoutInflater.from(container.context)

    private val listeners = mutableListOf<UpdateListener>()
    private var items: List<ViewModelItem> = emptyList()

    private val viewModels: List<IViewModel>
        get() {
            return simpleLazyListOf(items) { it.viewModel }
        }

    interface UpdateListener {
        fun onDataChanged(items: List<ViewModelItem>, changeInfo: Any?)
    }

    fun addListener(listener: UpdateListener) {
        listeners.add(listener)
        listener.onDataChanged(items, null)
    }

    fun getSize(): Int {
        return items.size
    }

    fun getItemAt(index: Int): ViewModelItem {
        return items[index]
    }

    fun getId(index: Int): Long {
        return items[index].viewModel.componentId
    }

    fun contains(item: ViewModelItem): Boolean {
        return items.contains(item)
    }

    override fun onDataChanged(items: List<ViewModelItem>, state: Any?, updateSpec: Any?) {
        this.items = items
        listeners.forEach {
            it.onDataChanged(items, updateSpec)
        }
    }

    fun indexOf(binder: ViewBinder<*>): Int {
        return items.indexOf(binder.viewModelItem)
    }

    private val boundViews = mutableMapOf<Long, ViewBinder<*>>()

    fun bindings(setup: ViewBinderAdapter.() -> Unit) {
        setup.invoke(viewBinderAdapter)
    }

    fun createView(typeId: Int, container: ViewGroup? = null): View {
        val binder = viewBinderAdapter.getBinder(typeId)
        val viewAdapter = binder as? ViewContainerViewAdapter ?: DefaultViewContainerViewAdapter
        return viewAdapter.onCreateDetachedView(
            this.container.context, binder, layoutInflater, container ?: this.container
        )
    }

    fun createView(viewModel: IViewModel, container: ViewGroup? = null): View {
        val binder = viewBinderAdapter.getBinder(viewModel)
        val viewAdapter = binder as? ViewContainerViewAdapter ?: DefaultViewContainerViewAdapter
        return viewAdapter.onCreateDetachedView(
            this.container.context, binder, layoutInflater, container ?: this.container
        )
    }

    fun getViewType(position: Int): Int {
        return viewBinderAdapter.getType(items[position].viewModel)
    }

    fun getViewType(viewModel: IViewModel): Int {
        return viewBinderAdapter.getType(viewModel)
    }

    fun bindView(position: Int, view: View): ViewBinder<*> {
        val viewModel = items[position]
        return bindView(viewModel, position, view)
    }

    fun bindView(item: ViewModelItem, view: View): ViewBinder<*> {
        val position = viewModels.indexOf(item.viewModel)
        return bindView(item, position, view)
    }

    private fun bindView(item: ViewModelItem, position: Int, view: View): ViewBinder<*> {
        val oldBinder: ViewBinder<*>? = boundViews[item.viewModel.componentId]
        styleAdapter.applyViewStyle(view, position, viewModels)
        return if (oldBinder != null) {
            if (oldBinder.viewModel != item.viewModel) {
                // TODO should not happen in RecyclerView
                // TODO need to check
                setFocused(oldBinder, false)
                setVisible(oldBinder, false)
                // END TODO
                unbindView(oldBinder)
                performNewBinding(item, view)
            } else {
                oldBinder
            }
        } else {
            performNewBinding(item, view)
        }.also {
            boundViews[item.viewModel.componentId] = it
        }
    }

    private fun performNewBinding(item: ViewModelItem, view: View): ViewBinder<*> {
        val binder = viewBinderAdapter.getBinder(item.viewModel)
        item.setBound(true)
        binder.performViewBinding(item, view)
        return binder
    }

    fun unbindView(viewBinder: ViewBinder<*>) {
        val viewModel = viewBinder.viewModel
        if (boundViews.containsKey(viewModel.componentId)) {
            viewBinder.viewModelItem.setBound(false)
            boundViews.remove(viewModel.componentId)
        }
    }

    fun setVisible(viewBinder: ViewBinder<*>, isVisible: Boolean) {
        viewBinder.viewModelItem.setVisible(isVisible)
    }

    fun setFocused(viewBinder: ViewBinder<*>, isFocused: Boolean) {
        viewBinder.viewModelItem.setFocused(isFocused)
    }

}