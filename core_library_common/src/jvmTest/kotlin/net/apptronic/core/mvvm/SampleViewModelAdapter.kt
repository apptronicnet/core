package net.apptronic.core.mvvm

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelStackAdapter
import net.apptronic.core.mvvm.viewmodel.navigation.TransitionInfo

class SampleViewModelAdapter : ViewModelStackAdapter() {

    var actualModel: ViewModel? = null

    override fun onInvalidate(newModel: ViewModel?, transitionInfo: TransitionInfo) {
        actualModel = newModel
    }

}