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

    /**
     * Defines whenever [ViewModel] should not be recycled on going out from list. This should be used carefully
     * as it may cause that [ViewModel] will not be recycled after [item] is removed from list. This method should
     * verify that [item] exists in list, which can be hard in case when list is large or lazy-loaded by itself so
     * simple check for [List.contains] can be dangerous as requires seeking whole list and loading all of it's elements.
     * Position in list is not provided too as it took to much time to define real position of item on long or lazy list.
     *
     * @param list list, used as source in adapter
     * @param item item, which represents [viewModel] in [list]
     * @param viewModel [ViewModel], used to display this item
     */
    fun shouldRetainInstance(list: List<T>, item: T, viewModel: VM): Boolean {
        return false
    }

}