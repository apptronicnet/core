package net.apptronic.core.mvvm.utils

import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.PredicateObserver
import net.apptronic.core.component.entity.Subscription
import net.apptronic.core.component.entity.base.UpdateAndStorePredicate
import net.apptronic.core.mvvm.viewmodel.ViewModel

/**
 * Class created to perform updates of list of [ViewModel].
 * By using this class you can prevent from recreating [ViewModel] for item in list
 * which only changed it's state but remains same instance (by id). In case if [ViewModel]
 * for item in updated list already exists - it will not create new [ViewModel] but update
 * existing [ViewModel] and place it in updates list at required place.
 */
abstract class ViewModelListBuilder<T, Id, VM : ViewModel> : Predicate<List<ViewModel>> {

    private inner class ViewModelHolder(
        val id: Id,
        val viewModel: VM
    )

    private val viewModelHolders = arrayListOf<ViewModelHolder>()

    private val predicate = UpdateAndStorePredicate<List<ViewModel>>()

    init {
        predicate.update(emptyList())
    }

    /**
     * Get id for item. By this id [ViewModelListBuilder] defines is item is same or not.
     */
    protected abstract fun getId(item: T): Id

    /**
     * Create [ViewModel] fro item
     */
    protected abstract fun onCreateViewModel(item: T): VM

    /**
     * Update already existing [ViewModel] for item
     */
    protected abstract fun onUpdateViewModel(viewModel: VM, newItem: T)

    /**
     * Update list of [ViewModel]s with new items.
     */
    fun update(newList: List<T>) {
        val oldIds = viewModelHolders.map { it.id }
        val newIds = newList.map { getId(it) }

        val newMap = hashMapOf<Id, T>()
        newList.forEach {
            newMap[getId(it)] = it
        }

        val same = oldIds.filter {
            newIds.contains(it)
        }.toSet()
        val added = newIds.filter {
            same.contains(it).not()
        }.toSet()
        val removed = oldIds.filter {
            same.contains(it).not()
        }.toSet()

        val addedItems = newList.filter {
            added.contains(getId(it))
        }

        viewModelHolders.removeAll { removed.contains(it.id) }
        viewModelHolders.forEach { viewModelHolder ->
            val id = viewModelHolder.id
            if (same.contains(id)) {
                val item = newMap[id]
                if (item != null) {
                    onUpdateViewModel(viewModelHolder.viewModel, item)
                }
            }
        }
        addedItems.forEach { item ->
            val id = getId(item)
            val viewModel = onCreateViewModel(item)
            viewModelHolders.add(ViewModelHolder(id, viewModel))
        }
        viewModelHolders.sortWith(PostArrangeComparator(newList))
        val result = viewModelHolders.map { it.viewModel }
        predicate.update(result)
    }

    override fun subscribe(observer: PredicateObserver<List<ViewModel>>): Subscription {
        return predicate.subscribe(observer)
    }

    private inner class PostArrangeComparator(items: List<T>) :
        Comparator<ViewModelHolder> {

        private val indexes = hashMapOf<Id, Int>()

        init {
            items.forEachIndexed { index, item ->
                indexes[getId(item)] = index
            }
        }

        override fun compare(left: ViewModelHolder, right: ViewModelHolder): Int {
            val leftIndex = indexes[left.id]
            val rightIndex = indexes[right.id]
            return if (leftIndex != null && rightIndex != null) {
                leftIndex.compareTo(rightIndex)
            } else 0
        }

    }

}