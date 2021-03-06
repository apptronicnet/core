package net.apptronic.core.viewmodel.navigation.adapters

import net.apptronic.core.viewmodel.navigation.ViewModelItem

interface ViewModelListAdapter<State> {

    fun onDataChanged(items: List<ViewModelItem>, state: State, updateSpec: Any?)

}