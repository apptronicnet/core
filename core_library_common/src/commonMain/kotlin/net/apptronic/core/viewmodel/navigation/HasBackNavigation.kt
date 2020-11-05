package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.viewmodel.ViewModel

/**
 * Interface declaring that [ViewModel] supports user input for back navigation
 */
interface HasBackNavigation {

    /**
     * Called when user is trying to start back navigation with input
     */
    fun getBackNavigationStatus(): BackNavigationStatus = BackNavigationStatus.Allowed

    /**
     * Called when back navigation transition was cancelled because of restriction
     */
    fun onBackNavigationRestrictedEvent() {
        // no default implementation
    }

    /**
     * Called when back navigation transition started to completing
     */
    fun onBackNavigationConfirmedEvent() {
        // no default implementation
    }

}