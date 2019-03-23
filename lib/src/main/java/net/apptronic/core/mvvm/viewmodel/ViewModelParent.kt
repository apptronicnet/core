package net.apptronic.core.mvvm.viewmodel

interface ViewModelParent {

    fun requestCloseSelf(viewModel: ViewModel, transitionInfo: Any? = null)

}