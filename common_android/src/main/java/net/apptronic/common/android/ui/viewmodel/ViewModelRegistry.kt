package net.apptronic.common.android.ui.viewmodel

import java.util.concurrent.atomic.AtomicLong

object ViewModelRegistry {

    const val NO_ID: Long = -1

    private val idGenerator = AtomicLong(0)
    private val models = HashMap<Long, ViewModel>()

    fun nextId(): Long {
        return idGenerator.getAndIncrement()
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