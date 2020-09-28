package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ItemStateNavigator
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

class TestListAdapter(
        private val itemStateNavigator: ItemStateNavigator
) : ViewModelListAdapter {

    var items: List<IViewModel> = emptyList()

    override fun onDataChanged(items: List<IViewModel>, changeInfo: Any?) {
        this.items = items
    }

    fun setFullBound(viewModel: IViewModel, state: Boolean) {
        if (state) {
            itemStateNavigator.setBound(viewModel, true)
            itemStateNavigator.setVisible(viewModel, true)
            itemStateNavigator.setFocused(viewModel, true)
        } else {
            itemStateNavigator.setFocused(viewModel, false)
            itemStateNavigator.setVisible(viewModel, false)
            itemStateNavigator.setBound(viewModel, false)
        }
    }

}