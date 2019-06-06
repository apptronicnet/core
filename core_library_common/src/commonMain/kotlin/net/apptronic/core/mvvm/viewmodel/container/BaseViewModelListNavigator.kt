package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

abstract class BaseViewModelListNavigator<T>(
    parent: ViewModel
) : Navigator<List<T>>(parent), ViewModelListAdapter.SourceNavigator {

    abstract fun setAdapter(adapter: ViewModelListAdapter)

}