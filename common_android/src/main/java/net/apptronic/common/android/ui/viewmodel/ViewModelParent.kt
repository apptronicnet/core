package net.apptronic.common.android.ui.viewmodel

interface ViewModelParent {

    fun requestCloseSelf(viewModel: ViewModel, transitionInfo: Any? = null)

}