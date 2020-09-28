package net.apptronic.core.mvvm.viewmodel.adapter

import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.TransitionInfo

/**
 * View model adapter is a class which creates view controller
 * for corresponding viewModel instance
 */
abstract class ViewModelStackAdapter {

    /**
     * Called when active viewModel for specific [ViewModelContainer] is changed
     * @param newModel viewModel which now active in stack. May be null if new model is not set
     * @param isNewOnFront defines is view for [newModel] should be on front
     * @param transitionInfo additional info to apply model change. It may contain info about
     * required animation etc.
     */
    abstract fun onInvalidate(newModel: IViewModel?, transitionInfo: TransitionInfo)

}