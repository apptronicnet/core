package net.apptronic.core.viewmodel

interface ViewModelParent {

    fun requestCloseSelf(viewModel: IViewModel, transitionInfo: Any? = null)

}