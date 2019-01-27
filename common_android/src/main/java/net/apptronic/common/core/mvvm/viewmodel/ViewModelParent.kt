package net.apptronic.common.core.mvvm.viewmodel

interface ViewModelParent {

    fun requestCloseSelf(viewModel: ViewModel, transitionInfo: Any? = null)

}