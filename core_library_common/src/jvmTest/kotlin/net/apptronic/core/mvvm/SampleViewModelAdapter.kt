package net.apptronic.core.mvvm

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelStackAdapter

class SampleViewModelAdapter : ViewModelStackAdapter() {

    var actualModel: ViewModel? = null

    override fun onInvalidate(newModel: ViewModel?, isNewOnFront: Boolean, transitionInfo: Any?) {
        actualModel = newModel
    }

}