package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.ViewModel

internal class ViewModelContainers<T, Id> {

    private val containers = mutableMapOf<Id, Record>()

     inner class Record(
             val id: Id,
             val container: ViewModelContainer,
             var item: T
    ) {
        var requiresUpdate = false
    }

    fun add(id: Id, container: ViewModelContainer, item: T) {
        containers[id] = Record(id, container, item)
    }

    fun markAllRequiresUpdate() {
        containers.forEach {
            it.value.requiresUpdate = true
        }
    }

    fun findRecordForModel(viewModel: ViewModel): Record? {
        return containers.values.firstOrNull {
            it.container.getViewModel() == viewModel
        }
    }

    fun findRecordForId(id: Id): Record? {
        return containers.values.firstOrNull {
            it.id == id
        }
    }

    fun removeById(id: Id): Record? {
        return containers.remove(id)
    }

    fun getAll(): List<Record> {
        return containers.values.toList()
    }

    fun terminateAllAndClear() {
        containers.values.forEach {
            it.container.terminate()
        }
        containers.clear()
    }

}