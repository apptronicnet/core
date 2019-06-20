package net.apptronic.core.mvvm.viewmodel.adapter

import net.apptronic.core.mvvm.viewmodel.ViewModel

abstract class ViewModelListAdapter {

    private val listeners = mutableListOf<() -> Unit>()
    private var items: List<ViewModel> = emptyList()
    private var navigator: ItemStateNavigator? = null

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

    fun indexOf(viewModel: ViewModel): Int {
        return items.indexOf(viewModel)
    }

    fun setNavigator(navigator: ItemStateNavigator?) {
        this.navigator = navigator
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

    protected fun setVisible(viewModel: ViewModel, isBound: Boolean) {
        navigator?.setVisible(viewModel, isBound)
    }

    protected fun setFocused(viewModel: ViewModel, isBound: Boolean) {
        navigator?.setFocused(viewModel, isBound)
    }

}