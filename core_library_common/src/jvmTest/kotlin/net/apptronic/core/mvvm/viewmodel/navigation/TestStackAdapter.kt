package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelStackAdapter

internal class TestStackAdapter : ViewModelStackAdapter() {

    var activeModel: ViewModel? = null
    var lastTransition: Any? = null

    override fun onInvalidate(oldModel: ViewModel?, newModel: ViewModel?, transitionInfo: Any?) {
        activeModel = newModel
        lastTransition = transitionInfo
    }

}