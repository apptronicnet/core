package net.apptronic.core.mvvm.viewmodel.adapter

import net.apptronic.core.mvvm.viewmodel.IViewModel

interface ViewModelListAdapter {

    fun onDataChanged(items: List<IViewModel>, changeInfo: Any?)

}