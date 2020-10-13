package net.apptronic.core.mvvm.viewmodel.adapter

import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelListItem

interface ViewModelListAdapter {

    fun onDataChanged(items: List<ViewModelListItem>, changeInfo: Any?)

}