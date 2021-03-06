package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.viewmodel.navigation.adapters.ViewModelListAdapter

class TestListAdapter : ViewModelListAdapter<Unit> {

    var items: List<ViewModelItem> = emptyList()

    override fun onDataChanged(items: List<ViewModelItem>, state: Unit, changeInfo: Any?) {
        this.items = items
    }

    fun setFullBound(viewModel: ViewModelItem, state: Boolean) {
        if (state) {
            viewModel.setBound(true)
            viewModel.setVisible(true)
            viewModel.setFocused(true)
        } else {
            viewModel.setFocused(false)
            viewModel.setVisible(false)
            viewModel.setBound(false)
        }
    }

}