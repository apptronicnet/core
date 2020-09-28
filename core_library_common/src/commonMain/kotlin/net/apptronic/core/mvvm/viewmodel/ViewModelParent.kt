package net.apptronic.core.mvvm.viewmodel

interface ViewModelParent {

    fun requestCloseSelf(viewModel: IViewModel, transitionInfo: Any? = null)

}