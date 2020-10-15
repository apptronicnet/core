package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.adapters.ViewModelListAdapter
import net.apptronic.core.mvvm.viewmodel.navigation.models.IListNavigationModel
import net.apptronic.core.mvvm.viewmodel.navigation.models.ListNavigatorContent

abstract class ListNavigator<Content : ListNavigatorContent<T, State>, T, State>(
        parent: IViewModel
) : Navigator<Content>(parent), IListNavigationModel<Content, T, State> {

    private var adapter: ViewModelListAdapter<in State>? = null

    final override fun setAdapter(adapter: ViewModelListAdapter<in State>) {
        this.adapter = adapter
        onSetAdapter(adapter)
        onNotifyAdapter(adapter, null)
        parentContext.lifecycle.onExitFromActiveStage {
            this.adapter = null
        }
    }

    protected abstract fun onSetAdapter(adapter: ViewModelListAdapter<in State>)

    protected fun notifyAdapter(updateSpec: Any?) {
        val adapter = this@ListNavigator.adapter
        if (adapter != null) {
            onNotifyAdapter(adapter, updateSpec)
        }
    }

    protected abstract fun onNotifyAdapter(adapter: ViewModelListAdapter<in State>, updateSpec: Any?)

}