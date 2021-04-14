package net.apptronic.core.viewmodel.dispatcher

/**
 * Declares class which is responsible for managing platform views
 */
interface ViewDispatcher {

    fun onNextViewModelDispatcher(viewModelDispatcher: ViewModelDispatcher<*>)

}