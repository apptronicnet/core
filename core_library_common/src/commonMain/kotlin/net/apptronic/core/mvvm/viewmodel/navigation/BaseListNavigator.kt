package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

abstract class BaseListNavigator<T>(
    parent: ViewModel
) : Navigator<List<T>>(parent) {

    abstract fun setAdapter(adapter: ViewModelListAdapter)

}