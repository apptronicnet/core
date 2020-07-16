package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelStackAdapter

internal class TestStackAdapter : ViewModelStackAdapter() {

    var activeModel: ViewModel? = null
    var lastTransition: Any? = null
    var lastOnFront: Boolean? = null

    override fun onInvalidate(newModel: ViewModel?, isNewOnFront: Boolean, transitionInfo: Any?) {
        activeModel = newModel
        lastOnFront = isNewOnFront
        lastTransition = transitionInfo
    }

}