package net.apptronic.common.android.mvvm.components.fragment

import net.apptronic.common.core.mvvm.viewmodel.ViewModel

interface ViewModelController {

    fun setViewModel(viewModel: ViewModel) {
        setViewModel(viewModel.getId())
    }

    fun setViewModel(id: Long)

}