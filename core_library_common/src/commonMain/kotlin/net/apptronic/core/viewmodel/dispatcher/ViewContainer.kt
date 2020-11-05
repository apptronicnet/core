package net.apptronic.core.viewmodel.dispatcher

import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModel

/**
 * Base interface for view container which can hold [ViewModel] and it's platform view.
 */
interface ViewContainer<T : IViewModel> {

    /**
     * Called when [ViewModelDispatcher] created/provided [ViewModel] for this [ViewContainer]
     */
    fun onAddedViewModel(viewModel: T)

    /**
     * Called when [ViewModel] requested to be closed. This should trigger closing platform app window, allowing
     * [ViewModel] to initiate "Close app" action by user request or by internal logic.
     */
    fun onViewModelRequestedCloseSelf()

}