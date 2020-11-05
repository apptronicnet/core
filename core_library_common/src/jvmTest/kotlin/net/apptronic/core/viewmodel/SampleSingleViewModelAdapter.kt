package net.apptronic.core.viewmodel

import net.apptronic.core.viewmodel.navigation.TransitionInfo
import net.apptronic.core.viewmodel.navigation.ViewModelItem
import net.apptronic.core.viewmodel.navigation.adapters.SingleViewModelAdapter

class SampleSingleViewModelAdapter : SingleViewModelAdapter {

    var actualModel: IViewModel? = null

    override fun onInvalidate(item: ViewModelItem?, transitionInfo: TransitionInfo) {
        actualModel = item?.viewModel
    }

}