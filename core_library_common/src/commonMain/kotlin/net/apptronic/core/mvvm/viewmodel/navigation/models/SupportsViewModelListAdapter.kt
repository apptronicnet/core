package net.apptronic.core.mvvm.viewmodel.navigation.models

import net.apptronic.core.mvvm.viewmodel.navigation.adapters.ViewModelListAdapter

interface SupportsViewModelListAdapter<State> {

    fun setAdapter(adapter: ViewModelListAdapter<in State>)

}