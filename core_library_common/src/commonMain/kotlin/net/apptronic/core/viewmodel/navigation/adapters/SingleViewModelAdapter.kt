package net.apptronic.core.viewmodel.navigation.adapters

import net.apptronic.core.viewmodel.navigation.Navigator
import net.apptronic.core.viewmodel.navigation.TransitionInfo
import net.apptronic.core.viewmodel.navigation.ViewModelItem

/**
 * View model adapter is a class which creates view controller
 * for corresponding viewModel instance
 */
interface SingleViewModelAdapter {

    /**
     * Called when active viewModel for specific [Navigator] is changed
     * @param item viewModel which now active in stack. May be null if new model is not set
     * @param transitionInfo additional info to apply model change. It may contain info about
     * required animation etc.
     */
    fun onInvalidate(item: ViewModelItem?, transitionInfo: TransitionInfo)

}