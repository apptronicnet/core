package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModel

/**
 * Allows to manage lifecycle or [ViewModel] according to it's container requirements
 */
internal interface ViewModelItemLifecycleController {

    fun setBound(viewModel: IViewModel, isBound: Boolean)

    fun setVisible(viewModel: IViewModel, isVisible: Boolean)

    fun setFocused(viewModel: IViewModel, isFocused: Boolean)

}