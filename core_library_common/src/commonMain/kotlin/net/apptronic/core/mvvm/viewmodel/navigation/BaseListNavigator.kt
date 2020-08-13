package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ItemStateNavigator
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

abstract class BaseListNavigator<T>(
        parent: ViewModel
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

    abstract fun getViewModels(): List<ViewModel>

    abstract fun getViewModelAt(index: Int): ViewModel

    abstract fun indexOfViewModel(viewModel: ViewModel): Int

    abstract override fun setBound(viewModel: ViewModel, isBound: Boolean)

    abstract override fun setVisible(viewModel: ViewModel, isVisible: Boolean)

    abstract override fun setFocused(viewModel: ViewModel, isFocused: Boolean)

}