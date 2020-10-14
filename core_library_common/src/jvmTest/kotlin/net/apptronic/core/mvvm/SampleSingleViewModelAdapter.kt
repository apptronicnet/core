package net.apptronic.core.mvvm

import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.SingleViewModelAdapter
import net.apptronic.core.mvvm.viewmodel.navigation.TransitionInfo
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelItem

class SampleSingleViewModelAdapter : SingleViewModelAdapter {

    var actualModel: IViewModel? = null

    override fun onInvalidate(item: ViewModelItem?, transitionInfo: TransitionInfo) {
        actualModel = item?.viewModel
    }

}