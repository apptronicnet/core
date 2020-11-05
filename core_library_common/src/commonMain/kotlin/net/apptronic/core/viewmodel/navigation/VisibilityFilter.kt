package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.functions.ofValue
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModel

/**
 * Interface to define is [ViewModel] ready to be visible in list or not. It can be used for display only items which
 * have data (hide empty items) or to show items only after data loaded.
 */
interface VisibilityFilter<VM : IViewModel> {

    /**
     * Check is [viewModel] ready to be shown in [Navigator]
     */
    fun isReadyToShow(viewModel: VM): Entity<Boolean> {
        return viewModel.ofValue(true)
    }

}