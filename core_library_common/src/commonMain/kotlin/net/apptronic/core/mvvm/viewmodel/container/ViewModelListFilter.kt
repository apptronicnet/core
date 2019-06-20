package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.mvvm.viewmodel.ViewModel

class ViewModelVisibilityRequest(val viewModel: ViewModel, val isVisible: Boolean)

interface ViewModelListFilter {

    fun filterList(source: List<ViewModelVisibilityRequest>): List<ViewModel>

}