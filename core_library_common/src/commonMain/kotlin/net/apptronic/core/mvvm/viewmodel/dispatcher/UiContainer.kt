package net.apptronic.core.mvvm.viewmodel.dispatcher

import net.apptronic.core.mvvm.viewmodel.ViewModel

interface UiContainer<T : ViewModel> {

    fun onAddedViewModel(viewModel: T)

    fun onViewModelRequestedCloseSelf()

}