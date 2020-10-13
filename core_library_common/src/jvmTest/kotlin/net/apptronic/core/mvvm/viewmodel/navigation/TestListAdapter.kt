package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

class TestListAdapter : ViewModelListAdapter {

    var items: List<ViewModelListItem> = emptyList()

    override fun onDataChanged(items: List<ViewModelListItem>, changeInfo: Any?) {
        this.items = items
    }

    fun setFullBound(viewModel: ViewModelListItem, state: Boolean) {
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