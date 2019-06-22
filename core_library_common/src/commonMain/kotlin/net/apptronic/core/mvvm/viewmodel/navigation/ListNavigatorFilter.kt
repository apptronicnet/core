package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Class which defines state of [ViewModel] visibility for [ListNavigator] returned by [VisibilityFilter]
 * @param viewModel [ViewModel] item
 * @param isVisible current value of [VisibilityFilter.isReadyToShow]
 */
class ItemVisibilityRequest(val viewModel: ViewModel, val isVisible: Boolean)

/**
 * This class defines behavior usage of [VisibilityFilter]. After each change items in [ListNavigator] or
 * change of visibility state of any of it's items [filterList] is called to receive filtered list of [ViewModel]s
 * to be displayed in [ListNavigator]
 */
interface ListNavigatorFilter {

    /**
     * Request to perform list filtering for [ListNavigator].
     * @param source contains list of [ItemVisibilityRequest] matching current content of [ListNavigator]
     * with each item visibility state. [ListNavigatorFilter] should decide how to interpreter this visibility. At least
     * all not visible items should not be shown as not all data for displaying on view may be ready here, but based on
     * desired UI behavior filter can decide to hide some additional items to make UI look and feel better.
     */
    fun filterList(source: List<ItemVisibilityRequest>): List<ViewModel>

}