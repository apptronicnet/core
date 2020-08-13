package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelStackAdapter

internal class TestStackAdapter : ViewModelStackAdapter() {

    var activeModel: ViewModel? = null
    var lastTransition: Any? = null
    var lastOnFront: Boolean? = null

    override fun onInvalidate(newModel: ViewModel?, transitionInfo: TransitionInfo) {
        activeModel = newModel
        lastOnFront = transitionInfo.isNewOnFront
        lastTransition = transitionInfo.spec
    }

}