package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Defines [ViewModel] which is attached to any type of [ListNavigator].
 */
class ViewModelItem internal constructor(
        private val container: ViewModelContainer,
        private val viewModelItemLifecycleController: ViewModelItemLifecycleController
) {

    /**
     * The [ViewModel] itself
     */
    val viewModel: IViewModel
        get() {
            return container.getViewModel()
        }

    /**
     * Tell [ViewModelItemLifecycleController] to set bound state of [ViewModel] inside navigator
     */
    fun setBound(isBound: Boolean) {
        viewModelItemLifecycleController.setBound(viewModel, isBound)
    }

    /**
     * Tell [ViewModelItemLifecycleController] to set visible state of [ViewModel] inside navigator
     */
    fun setVisible(isVisible: Boolean) {
        viewModelItemLifecycleController.setVisible(viewModel, isVisible)
    }

    /**
     * Tell [ViewModelItemLifecycleController] to set focused state of [ViewModel] inside navigator
     */
    fun setFocused(isFocused: Boolean) {
        viewModelItemLifecycleController.setFocused(viewModel, isFocused)
    }

    override fun equals(other: Any?): Boolean {
        return other is ViewModelItem && other.viewModel == viewModel
    }

    override fun hashCode(): Int {
        return viewModel.hashCode()
    }

}