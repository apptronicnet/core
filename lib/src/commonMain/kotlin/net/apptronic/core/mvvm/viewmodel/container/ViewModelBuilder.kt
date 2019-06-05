package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Class created to perform updates of list of [ViewModel].
 * By using this class you can prevent from recreating [ViewModel] for item in list
 * which only changed it's state but remains same instance (by id). In case if [ViewModel]
 * for item in updated list already exists - it will not create new [ViewModel] but update
 * existing [ViewModel] and place it in updates list at required place.
 */
interface ViewModelBuilder<T, Id, VM : ViewModel> {

    /**
     * Get id for item. By this id [ViewModelBuilder] defines is item is same or not.
     */
    fun getId(item: T): Id

    /**
     * Create [ViewModel] fro item
     */
    fun onCreateViewModel(parent: Context, item: T): VM

    /**
     * Update already existing [ViewModel] for item
     */
    fun onUpdateViewModel(viewModel: VM, newItem: T)

}