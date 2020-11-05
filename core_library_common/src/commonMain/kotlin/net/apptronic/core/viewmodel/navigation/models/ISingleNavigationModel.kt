package net.apptronic.core.viewmodel.navigation.models

import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.navigation.INavigator

interface ISingleNavigationModel : INavigator<SingleItemNavigatorContent>,
        SupportsSingleViewModelAdapter, SupportsSingleViewModelListAdapter {

    /**
     * Current count of [IViewModel]s inside
     */
    val size: Int
        get() {
            return content.get().size
        }

    /**
     * Current list of [IViewModel]s
     */
    val items: List<IViewModel>
        get() {
            return content.get().items
        }

    /**
     * Current actual [IViewModel] which should be visible ti user
     */
    val viewModel: IViewModel?
        get() {
            return content.get().viewModel
        }

    /**
     * Index of current actual [IViewModel] which should be visible ti user
     */
    val visibleIndex: Int
        get() {
            return content.get().visibleIndex
        }

    /**
     * Index of last [IViewModel] in this navigator
     */
    val lastIndex: Int
        get() {
            return size - 1
        }

    /**
     * Notify navigator that user is navigated to specific [index] of stack using UI interaction
     */
    fun onNavigated(index: Int)

}