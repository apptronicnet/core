package net.apptronic.core.mvvm.viewmodel.adapter

import net.apptronic.core.mvvm.viewmodel.ViewModel

abstract class ViewModelListAdapter {

    private val listeners = mutableListOf<() -> Unit>()
    private var items: List<ViewModel> = emptyList()
    private var navigator: SourceNavigator? = null

    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    fun getItems(): List<ViewModel> {
        return items
    }

    fun getSize(): Int {
        return items.size
    }

    fun getItemAt(position: Int): ViewModel {
        return items[position]
    }

    fun setNavigator(navigator: SourceNavigator?) {
        this.navigator = navigator
    }

    interface SourceNavigator {

        fun setBound(viewModel: ViewModel, isBound: Boolean)

    }

    fun onDataChanged(items: List<ViewModel>) {
        this.items = items
        listeners.forEach {
            it.invoke()
        }
    }

    protected fun setBound(viewModel: ViewModel, isBound: Boolean) {
        navigator?.setBound(viewModel, isBound)
    }

}