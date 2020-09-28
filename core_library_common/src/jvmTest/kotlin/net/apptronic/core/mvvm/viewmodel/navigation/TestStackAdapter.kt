package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelStackAdapter

internal class TestStackAdapter : ViewModelStackAdapter() {

    var activeModel: IViewModel? = null
    var lastTransition: Any? = null
    var lastOnFront: Boolean? = null

    override fun onInvalidate(newModel: IViewModel?, transitionInfo: TransitionInfo) {
        activeModel = newModel
        lastOnFront = transitionInfo.isNewOnFront
        lastTransition = transitionInfo.spec
    }

}