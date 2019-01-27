package net.apptronic.common.core.mvvm.viewmodel

import net.apptronic.common.core.base.AtomicEntity

object ViewModelRegistry {

    const val NO_ID: Long = -1

    private val idGenerator = AtomicEntity<Long>(0)
    private val models = mutableMapOf<Long, ViewModel>()

    fun nextId(): Long {
        return idGenerator.perform {
            set(it + 1L)
        }
    }

    fun add(viewModel: ViewModel) {
        models[viewModel.getId()] = viewModel
    }

    fun remove(viewModel: ViewModel) {
        models.remove(viewModel.getId())
    }

    fun get(id: Long): ViewModel? {
        return models[id]
    }

    fun <T : ViewModel> obtain(idSource: () -> Long) = lazy<T> {
        get(idSource()) as T
    }

}