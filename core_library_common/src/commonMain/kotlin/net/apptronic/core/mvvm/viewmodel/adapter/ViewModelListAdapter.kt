package net.apptronic.core.mvvm.viewmodel.adapter

import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelItem

interface ViewModelListAdapter<State> {

    fun onDataChanged(items: List<ViewModelItem>, state: State, changeInfo: Any?)

}