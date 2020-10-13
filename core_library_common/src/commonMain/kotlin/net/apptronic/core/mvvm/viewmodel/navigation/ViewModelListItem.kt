package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Defines [ViewModel] which is attached to any type of [BaseListNavigator].
 */
class ViewModelListItem internal constructor(
        private val container: ViewModelContainer,
        private val itemStateNavigator: ItemStateNavigator
) {

    /**
     * The [ViewModel] itself
     */
    val viewModel: IViewModel
        get() {
            return container.getViewModel()
        }

    /**
     * Tell [ItemStateNavigator] to set bound state of [ViewModel] inside navigator
     */
    fun setBound(isBound: Boolean) {
        itemStateNavigator.setBound(viewModel, isBound)
    }

    /**
     * Tell [ItemStateNavigator] to set visible state of [ViewModel] inside navigator
     */
    fun setVisible(isVisible: Boolean) {
        itemStateNavigator.setVisible(viewModel, isVisible)
    }

    /**
     * Tell [ItemStateNavigator] to set focused state of [ViewModel] inside navigator
     */
    fun setFocused(isFocused: Boolean) {
        itemStateNavigator.setFocused(viewModel, isFocused)
    }

    override fun equals(other: Any?): Boolean {
        return other is ViewModelListItem && other.viewModel == viewModel
    }

    override fun hashCode(): Int {
        return viewModel.hashCode()
    }

}