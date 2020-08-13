package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Defines order of views when performing transition for change
 */
enum class StackTransition {
    /**
     * Defines order automatically based on indexes of old [ViewModel] and new [ViewModel] inside of [StackNavigationModel]
     */
    Auto,

    /**
     * Force place new view on top, independently of [ViewModel]s order in [StackNavigationModel]
     */
    NewOnFront,

    /**
     * Force place new view on back, independently of [ViewModel]s order in [StackNavigationModel]
     */
    NewOnBack
}