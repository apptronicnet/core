package net.apptronic.core.mvvm.common

import net.apptronic.core.component.asEvent
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.behavior.onSubscribe
import net.apptronic.core.component.mutableValue
import net.apptronic.core.component.typedEvent
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

fun <T, Id> ViewModel.listSelector(getId: (T) -> Id): ListSelectorViewModel<T, Id> {
    return ListSelectorViewModel(this, getId)
}

fun <T> ViewModel.listSelector(): ListSelectorViewModel<T, T> {
    return ListSelectorViewModel(this) { it }
}

open class ListSelectorViewModel<T, Id> : ViewModel {

    private val getId: (T) -> Id

    constructor(context: ViewModelContext, getId: (T) -> Id) : super(context) {
        this.getId = getId
    }

    constructor(parent: ViewModel, getId: (T) -> Id) : super(parent) {
        this.getId = getId
    }

    internal val items = mutableValue<List<T>>(emptyList())
    internal val selectedItem = value<T?>(null)
    internal val selectedItemId = value<Id?>(null)

    internal val selectedItemUpdateEvent = typedEvent<T?>()
    internal val selectedItemIdUpdateEvent = typedEvent<Id?>()

    internal fun updateSelectedItem(item: T?) {
        selectedItem.set(item)
        selectedItemId.set(if (item != null) getId(item) else null)
    }

    fun setItems(items: List<T>) {
        this.items.set(items)
        calculateSelectedItemById()
        notifyBindings()
    }

    fun setSelectedItem(item: T?) {
        selectedItem.set(item)
        selectedItemId.set(if (item != null) getId(item) else null)
        notifyBindings()
    }

    fun setSelectedId(id: Id?) {
        selectedItemId.set(id)
        calculateSelectedItemById()
        notifyBindings()
    }

    fun items(): Entity<List<T>> {
        return items
    }

    fun selectedItem(): Entity<T?> {
        return selectedItem
    }

    fun selectedItemId(): Entity<Id?> {
        return selectedItemId
    }

    fun getItems(): List<T> {
        return items.getOr(emptyList())
    }

    fun getSelectedItem(): T? {
        return selectedItem.getOr(null)
    }

    fun getSelectedItemId(): Id? {
        return selectedItemId.getOr(null)
    }

    internal fun calculateSelectedItemById() {
        val id = selectedItemId.getOrNull()
        if (id == null) {
            selectedItem.set(null)
        } else {
            selectedItem.set(
                    items.getOr(emptyList()).firstOrNull {
                        getId(it) == id
                    }
            )
        }
    }

    private fun notifyBindings() {
        selectedItemUpdateEvent.sendEvent(selectedItem.getOrNull())
        selectedItemIdUpdateEvent.sendEvent(selectedItemId.getOrNull())
    }

    fun getBindingModel(): ListSelectorBindingModel<T, Id> {
        return ListSelectorBindingModel(this)
    }

}

class ListSelectorBindingModel<T, Id>(
        private val target: ListSelectorViewModel<T, Id>
) {

    fun observeItems(): Entity<List<T>> {
        return target.items
    }

    fun observeSelectedItem(recursive: Boolean): Entity<T?> {
        return if (recursive) {
            target.selectedItem
        } else {
            target.selectedItemUpdateEvent.asEvent().onSubscribe<T?> {
                target.selectedItem.getOrNull()
            }
        }
    }

    fun observeSelectedId(recursive: Boolean): Entity<Id?> {
        return if (recursive) {
            target.selectedItemId
        } else {
            target.selectedItemIdUpdateEvent.asEvent().onSubscribe<Id?> {
                target.selectedItemId.getOrNull()
            }
        }
    }

    fun onItemSelected(item: T?) {
        target.updateSelectedItem(item)
    }

    fun onItemIdSelected(id: Id?) {
        target.selectedItemId.set(id)
        target.calculateSelectedItemById()
    }

}