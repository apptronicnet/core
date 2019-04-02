package net.apptronic.mvvm

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelAdapter

class SampleViewModelAdapter : ViewModelAdapter() {

    var actualModel: ViewModel? = null

    override fun onInvalidate(oldModel: ViewModel?, newModel: ViewModel?, transitionInfo: Any?) {
        actualModel = newModel
    }

}