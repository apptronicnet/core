package net.apptronic.core.mvvm.viewmodel.adapter

import net.apptronic.core.mvvm.viewmodel.ViewModel

interface ViewModelListAdapter {

    fun onDataChanged(items: List<ViewModel>, changeInfo: Any?)

}