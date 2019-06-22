package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

class TestListAdapter : ViewModelListAdapter() {

    fun setFullBound(viewModel: ViewModel, state: Boolean) {
        if (state) {
            setBound(viewModel, true)
            setVisible(viewModel, true)
            setFocused(viewModel, true)
        } else {
            setFocused(viewModel, false)
            setVisible(viewModel, false)
            setBound(viewModel, false)
        }
    }

}