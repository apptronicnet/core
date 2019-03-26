package net.apptronic.core.mvvm.viewmodel.adapter

import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * View model adapter is a class which reflects creates view controller
 * for corresponding viewModel instance
 */
abstract class ViewModelAdapter {

    /**
     * Called when active viewModel for specific [ViewModelContainer] is changed
     * @param oldModel previous model active in stack. May be null if no model was active
     * @param newModel viewModel which now active in stack. May be null if new model is not
     * set
     * @param transitionInfo additional info to apply model change. It may contain info about
     * required animation etc.
     */
    abstract fun onInvalidate(
        oldModel: ViewModel?,
        newModel: ViewModel?,
        transitionInfo: Any?
    )

}