package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.mvvm.viewmodel.ViewModel

internal class ViewModelContainers<T, Id> {

    private val containers = mutableMapOf<Id, Record>()

     inner class Record(
            val id: Id,
            val container: ViewModelContainerItem,
            var item: T
    ) {
        var requiresUpdate = false
    }

    fun add(id: Id, container: ViewModelContainerItem, item: T) {
        containers[id] = Record(id, container, item)
    }

    fun markAllRequiresUpdate() {
        containers.forEach {
            it.value.requiresUpdate = true
        }
    }

    fun findRecordForModel(viewModel: ViewModel): Record? {
        return containers.values.firstOrNull {
            it.container.viewModel == viewModel
        }
    }

    fun findRecordForId(id: Id): Record? {
        return containers.values.firstOrNull {
            it.id == id
        }
    }

    fun findRecordForItem(item: T): Record? {
        return containers.values.firstOrNull {
            it.item == item
        }
    }

    fun removeById(id: Id): Record? {
        return containers.remove(id)
    }

    fun getAll(): List<Record> {
        return containers.values.toList()
    }

    fun clear() {
        containers.clear()
    }

}