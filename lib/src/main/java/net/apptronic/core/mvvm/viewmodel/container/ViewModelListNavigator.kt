package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.component.entity.base.UpdateAndStorePredicate
import net.apptronic.core.component.entity.entities.Property
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelParent
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

class ViewModelListNavigator(
    private val parent: ViewModel
) : Property<List<ViewModel>>(parent, UpdateAndStorePredicate()), ViewModelParent,
    ViewModelListAdapter.SourceNavigator {

    private var adapter: ViewModelListAdapter? = null

    private var items: List<ViewModel> = emptyList()

    private val containers = mutableMapOf<Long, ViewModelContainerItem>()

    override fun isSet(): Boolean {
        return true
    }

    override fun onGetValue(): List<ViewModel> {
        return items
    }

    private fun ViewModel.getContainer(): ViewModelContainerItem? {
        return containers[getId()]
    }

    fun update(action: (MutableList<ViewModel>) -> Unit) {
        val list = get().toMutableList()
        action.invoke(list)
        set(list)
    }

    override fun onSetValue(value: List<ViewModel>) {
        val diff = getDiff(get(), value)
        diff.removed.forEach {
            onRemoved(it)
        }
        items = value
        diff.added.forEach {
            onAdded(it)
        }
        adapter?.onDataChanged(get())
    }

    private fun onAdded(viewModel: ViewModel) {
        val container = ViewModelContainerItem(viewModel, parent)
        containers[viewModel.getId()] = container
        container.viewModel.onAttachToParent(this)
        container.setCreated(true)
    }

    private fun onRemoved(viewModel: ViewModel) {
        viewModel.getContainer()?.let { container ->
            container.viewModel.onDetachFromParent()
            container.terminate()
        }
    }

    override fun requestCloseSelf(
        viewModel: ViewModel,
        transitionInfo: Any?
    ) {
        // ignore
    }

    override fun setBound(viewModel: ViewModel, isBound: Boolean) {
        viewModel.getContainer()?.let {
            it.setBound(isBound)
        }
    }

    override fun setVisible(viewModel: ViewModel, isBound: Boolean) {
        viewModel.getContainer()?.let {
            it.setVisible(isBound)
        }
    }

    override fun setFocused(viewModel: ViewModel, isBound: Boolean) {
        viewModel.getContainer()?.let {
            it.setFocused(isBound)
        }
    }

    fun setAdapter(adapter: ViewModelListAdapter) {
        this.adapter = adapter
        adapter.onDataChanged(get())
        adapter.setNavigator(this)
        parent.getLifecycle().onExitFromActiveStage {
            items.forEach {
                setBound(it, false)
            }
            adapter.setNavigator(null)
            this.adapter = null
        }
    }

}