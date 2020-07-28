package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

abstract class BaseListNavigator<T>(
        parent: ViewModel
) : Navigator<List<T>>(parent) {

    private var adapter: ViewModelListAdapter? = null

    fun setAdapter(adapter: ViewModelListAdapter) {
        this.adapter = adapter
        onSetAdapter(adapter)
        onNotifyAdapter(adapter)
        context.lifecycle.onExitFromActiveStage {
            this.adapter = null
        }
    }

    protected abstract fun onSetAdapter(adapter: ViewModelListAdapter)

    fun notifyAdapter() {
        val adapter = this@BaseListNavigator.adapter
        if (adapter != null) {
            onNotifyAdapter(adapter)
        }
    }

    protected abstract fun onNotifyAdapter(adapter: ViewModelListAdapter)

}