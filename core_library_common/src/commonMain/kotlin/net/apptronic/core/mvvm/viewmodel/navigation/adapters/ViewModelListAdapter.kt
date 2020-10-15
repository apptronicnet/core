package net.apptronic.core.mvvm.viewmodel.navigation.adapters

import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelItem

interface ViewModelListAdapter<State> {

    fun onDataChanged(items: List<ViewModelItem>, state: State, updateSpec: Any?)

}