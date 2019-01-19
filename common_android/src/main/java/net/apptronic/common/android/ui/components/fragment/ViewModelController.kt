package net.apptronic.common.android.ui.components.fragment

import net.apptronic.common.android.ui.viewmodel.ViewModel

interface ViewModelController {

    fun setViewModel(viewModel: ViewModel) {
        setViewModel(viewModel.getId())
    }

    fun setViewModel(id: Long)

}