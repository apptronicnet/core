package net.apptronic.core.viewmodel.navigation.models

import net.apptronic.core.viewmodel.navigation.adapters.ViewModelListAdapter

interface SupportsViewModelListAdapter<State> {

    fun setAdapter(adapter: ViewModelListAdapter<in State>)

}