package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

abstract class BaseListNavigator<T>(
        parent: IViewModel
) : Navigator<List<T>>(parent), ItemStateNavigator {

    private var adapter: ViewModelListAdapter? = null

    fun setAdapter(adapter: ViewModelListAdapter) {
        this.adapter = adapter
        onSetAdapter(adapter)
        onNotifyAdapter(adapter, null)
        context.lifecycle.onExitFromActiveStage {
            this.adapter = null
        }
    }

    protected abstract fun onSetAdapter(adapter: ViewModelListAdapter)

    protected fun notifyAdapter(changeInfo: Any?) {
        val adapter = this@BaseListNavigator.adapter
        if (adapter != null) {
            onNotifyAdapter(adapter, changeInfo)
        }
    }

    protected abstract fun onNotifyAdapter(adapter: ViewModelListAdapter, changeInfo: Any?)

    abstract fun getSize(): Int

    abstract fun getViewModelItems(): List<ViewModelListItem>

    abstract fun getViewModelItemAt(index: Int): ViewModelListItem

    fun getViewModelItemAtOrNull(index: Int): ViewModelListItem? {
        return if (index in 0 until getSize()) {
            getViewModelItemAt(index)
        } else null
    }

    abstract fun indexOfViewModel(viewModel: IViewModel): Int

    abstract override fun setBound(viewModel: IViewModel, isBound: Boolean)

    abstract override fun setVisible(viewModel: IViewModel, isVisible: Boolean)

    abstract override fun setFocused(viewModel: IViewModel, isFocused: Boolean)

}