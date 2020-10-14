package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

abstract class BaseListNavigator<Content, State>(
        parent: IViewModel
) : Navigator<Content>(parent), ItemStateNavigator {

    private var adapter: ViewModelListAdapter<State>? = null

    fun setAdapter(adapter: ViewModelListAdapter<State>) {
        this.adapter = adapter
        onSetAdapter(adapter)
        onNotifyAdapter(adapter, null)
        context.lifecycle.onExitFromActiveStage {
            this.adapter = null
        }
    }

    protected abstract fun onSetAdapter(adapter: ViewModelListAdapter<State>)

    protected fun notifyAdapter(changeInfo: Any?) {
        val adapter = this@BaseListNavigator.adapter
        if (adapter != null) {
            onNotifyAdapter(adapter, changeInfo)
        }
    }

    protected abstract fun onNotifyAdapter(adapter: ViewModelListAdapter<State>, changeInfo: Any?)

    abstract fun getSize(): Int

    abstract fun getState(): State

    abstract fun getViewModelItems(): List<ViewModelItem>

    abstract fun getViewModelItemAt(index: Int): ViewModelItem

    fun getViewModelItemAtOrNull(index: Int): ViewModelItem? {
        return if (index in 0 until getSize()) {
            getViewModelItemAt(index)
        } else null
    }

    abstract fun indexOfViewModel(viewModel: IViewModel): Int

    abstract override fun setBound(viewModel: IViewModel, isBound: Boolean)

    abstract override fun setVisible(viewModel: IViewModel, isVisible: Boolean)

    abstract override fun setFocused(viewModel: IViewModel, isFocused: Boolean)

}