package net.apptronic.common.android.mvvm.components.fragment

import net.apptronic.common.core.component.Component

interface ViewModelController {

    fun setViewModel(viewModel: Component) {
        setViewModel(viewModel.getId())
    }

    fun setViewModel(id: Long)

}