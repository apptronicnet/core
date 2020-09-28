package net.apptronic.core.mvvm

import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelStackAdapter
import net.apptronic.core.mvvm.viewmodel.navigation.TransitionInfo

class SampleViewModelAdapter : ViewModelStackAdapter() {

    var actualModel: IViewModel? = null

    override fun onInvalidate(newModel: IViewModel?, transitionInfo: TransitionInfo) {
        actualModel = newModel
    }

}