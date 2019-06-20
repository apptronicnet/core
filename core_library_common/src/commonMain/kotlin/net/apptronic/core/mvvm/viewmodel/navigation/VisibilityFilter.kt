package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.ofValue
import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Interface to define is [ViewModel] ready to be visible in list or not. It can be used for display only items which
 * have data (hide empty items) or to show items only after data loaded.
 */
interface VisibilityFilter<VM : ViewModel> {

    /**
     * Check is [viewModel] ready to be shown in [Navigator]
     */
    fun isReadyToShow(viewModel: VM): Entity<Boolean> {
        return viewModel.ofValue(true)
    }

}