package net.apptronic.core.viewmodel.dispatcher

import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModel
import kotlin.reflect.KClass

/**
 * Class which managing root [ViewModel] state and provides it to [ViewContainer]
 */
interface ViewModelDispatcher<T : IViewModel> {

    /**
     * Get type of view model for this dispatcher
     */
    fun viewModelType(): KClass<T>

    /**
     * Check is [ViewModelDispatcher] have active view model or not
     */
    fun haveActiveViewModel(): Boolean

    /**
     * Get currently active [ViewModel]. If currently no [ViewModel] active - it will be automatically created.
     */
    fun getViewModel(): T

    /**
     * Register [ViewContainer] which requires to bind to [ViewModel]. Uses currently active [ViewModel] or creates it
     * if it is not present.
     */
    fun registerContainer(container: ViewContainer<T>)

    /**
     * Releases [ViewContainer], meaning there is no UI which can be bound to active [ViewModel]. This is not destroying
     * [ViewModel]. To terminate [ViewModel] it needed to explicitly call [recycleViewModel]
     */
    fun unregisterContainer(container: ViewContainer<T>)

    /**
     * Recycles active [ViewModel] if it is present.
     */
    fun recycleViewModel()

}