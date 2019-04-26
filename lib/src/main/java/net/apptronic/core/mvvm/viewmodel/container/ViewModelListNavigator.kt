package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.component.entity.base.UpdateAndStorePredicate
import net.apptronic.core.component.entity.entities.Property
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelParent
import net.apptronic.core.mvvm.viewmodel.adapter.RangeChangedListener
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

class ViewModelListNavigator(
    private val parent: ViewModel
) : Property<List<ViewModel>>(parent, UpdateAndStorePredicate()), ViewModelParent {

    private var adapter: ViewModelListAdapter? = null

    private val items = mutableListOf<ViewModelContainerItem>()

    override fun isSet(): Boolean {
        return true
    }

    override fun onGetValue(): List<ViewModel> {
        return items.map { it.viewModel }
    }

    fun update(action: (MutableList<ViewModel>) -> Unit) {
        val list = get().toMutableList()
        action.invoke(list)
        set(list)
    }

    private fun ViewModel.container(): ViewModelContainerItem? {
        return items.firstOrNull {
            it.viewModel == this
        }
    }

    override fun onSetValue(value: List<ViewModel>) {
        val diff = getDiff(get(), value)
        diff.removed
            .mapNotNull { it.container() }
            .forEach {
                onRemoved(it)
            }
        items.clear()
        items.addAll(value.map { ViewModelContainerItem(it, parent) })
        diff.added
            .mapNotNull { it.container() }
            .forEach {
                onAdded(it)
            }
        adapter?.updateDataSet(get())
        adapter?.requestVisibleRange()
    }

    private fun onAdded(item: ViewModelContainerItem) {
        item.viewModel.onAttachToParent(this)
        item.setCreated(true)
    }

    private fun onRemoved(item: ViewModelContainerItem) {
        item.viewModel.onDetachFromParent()
        item.terminate()
    }

    override fun requestCloseSelf(
        viewModel: ViewModel,
        transitionInfo: Any?
    ) {
        // ignore
    }

    private val rangeChangedListener = object :
        RangeChangedListener {

        override fun onVisibleRangeChanged(start: Int, end: Int) {
            items.forEachIndexed { index, item ->
                val isInVisibleRange = index in start until end
                item.setBound(isInVisibleRange)
                item.setVisible(isInVisibleRange)
                item.setFocused(isInVisibleRange)
            }
        }

    }

    fun setAdapter(adapter: ViewModelListAdapter) {
        this.adapter?.let {
            it.setRangeChangedListener(null)
        }
        this.adapter = adapter
        adapter.setRangeChangedListener(rangeChangedListener)
        adapter.updateDataSet(get())
        adapter.requestVisibleRange()
        parent.getLifecycle().onExitFromActiveStage {
            adapter.setRangeChangedListener(null)
            this.adapter = null
        }
    }

}