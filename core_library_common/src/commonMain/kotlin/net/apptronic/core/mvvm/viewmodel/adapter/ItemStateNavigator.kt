package net.apptronic.core.mvvm.viewmodel.adapter

import net.apptronic.core.mvvm.viewmodel.IViewModel

interface ItemStateNavigator {

    fun setBound(viewModel: IViewModel, isBound: Boolean)

    fun setVisible(viewModel: IViewModel, isVisible: Boolean)

    fun setFocused(viewModel: IViewModel, isFocused: Boolean)

}