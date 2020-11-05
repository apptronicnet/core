package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.navigation.adapters.SingleViewModelAdapter

internal class TestAdapterSingle : SingleViewModelAdapter {

    var activeModel: IViewModel? = null
    var lastTransition: Any? = null
    var lastOnFront: Boolean? = null

    override fun onInvalidate(item: ViewModelItem?, transitionInfo: TransitionInfo) {
        activeModel = item?.viewModel
        lastOnFront = transitionInfo.isNewOnFront
        lastTransition = transitionInfo.spec
    }

}