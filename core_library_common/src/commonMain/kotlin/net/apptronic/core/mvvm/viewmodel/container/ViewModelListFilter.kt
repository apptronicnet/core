package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Class which defines state of [ViewModel] visibility for [ViewModelListNavigator] returned by [VisibilityFilter]
 * @param viewModel [ViewModel] item
 * @param isVisible current value of [VisibilityFilter.isReadyToShow]
 */
class ViewModelVisibilityRequest(val viewModel: ViewModel, val isVisible: Boolean)

/**
 * This class defines behavior usage of [VisibilityFilter]. After each change items in [ViewModelListNavigator] or
 * change of visibility state of any of it's items [filterList] is called to receive filtered list of [ViewModel]s
 * to be displayed in [ViewModelListNavigator]
 */
interface ViewModelListFilter {

    /**
     * Request to perform list filtering for [ViewModelListNavigator].
     * @param source contains list of [ViewModelVisibilityRequest] matching current content of [ViewModelListNavigator]
     * with each item visibility state. [ViewModelListFilter] should decide how to interpreter this visibility. At least
     * all not visible items should not be shown as not all data for displaying on view may be ready here, but based on
     * desired UI behavior filter can decide to hide some additional items to make UI look and feel better.
     */
    fun filterList(source: List<ViewModelVisibilityRequest>): List<ViewModel>

}