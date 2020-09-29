package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.onchange.Next
import net.apptronic.core.component.entity.onchange.OnChangeProperty
import net.apptronic.core.component.entity.onchange.OnChangeValue
import net.apptronic.core.component.entity.onchange.onChangeValue
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel

fun <T, Id, VM : IViewModel> Contextual.viewModelListBuilder(builder: ViewModelBuilder<T, Id, VM>): ViewModelListBuilder<T, Id, VM> {
    return ViewModelListBuilder(context, builder)
}

/**
 * Class created to perform updates of list of [ViewModel].
 * By using this class you can prevent from recreating [ViewModel] for item in list
 * which only changed it's state but remains same instance (by id). In case if [ViewModel]
 * for item in updated list already exists - it will not create new [ViewModel] but update
 * existing [ViewModel] and place it in updates list at required place.
 */
class ViewModelListBuilder<T, Id, VM : IViewModel> internal constructor(
        private val builderContext: Context,
        private val builder: ViewModelBuilder<T, Id, VM>,
        private val onChangeValue: OnChangeValue<List<IViewModel>, Any> = builderContext.onChangeValue()
) : ViewModelBuilder<T, Id, VM> by builder, OnChangeProperty<List<IViewModel>, Any> by onChangeValue {

    private inner class ViewModelHolder(
            val id: Id,
            val viewModel: VM
    )

    private val viewModelHolders = arrayListOf<ViewModelHolder>()

    private val viewModels = builderContext.onChangeValue<List<IViewModel>, Any>()

    /**
     * Update list of [ViewModel]s automatically from given [Entity]
     */
    fun updateFrom(entity: Entity<out List<T>>) {
        entity.subscribe { update(it) }
    }

    /**
     * Update list of [ViewModel]s automatically from given [Entity]
     */
    fun updateFromChanges(entity: Entity<out Next<out List<T>, out Any>>) {
        entity.subscribe { update(it.value, it.change) }
    }

    /**
     * Update list of [ViewModel]s with new items.
     */
    fun update(newList: List<T>, changeInfo: Any? = null) {
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
            val viewModel = onCreateViewModel(context, item)
            viewModelHolders.add(ViewModelHolder(id, viewModel))
        }
        viewModelHolders.sortWith(PostArrangeComparator(newList))
        val result = viewModelHolders.map { it.viewModel }
        onChangeValue.set(result, changeInfo)
    }

    private inner class PostArrangeComparator(items: List<T>) :
            Comparator<ViewModelHolder> {

        private val indexes = hashMapOf<Id, Int>()

        init {
            items.forEachIndexed { index, item ->
                indexes[getId(item)] = index
            }
        }

        override fun compare(a: ViewModelHolder, b: ViewModelHolder): Int {
            val aIndex = indexes[a.id]
            val bIndex = indexes[b.id]
            return if (aIndex != null && bIndex != null) {
                aIndex.compareTo(bIndex)
            } else 0
        }

    }

}