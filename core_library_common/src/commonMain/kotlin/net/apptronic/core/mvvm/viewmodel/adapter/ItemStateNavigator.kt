package net.apptronic.core.mvvm.viewmodel.adapter

import net.apptronic.core.mvvm.viewmodel.ViewModel

interface ItemStateNavigator {

    fun setBound(viewModel: ViewModel, isBound: Boolean)

    fun setVisible(viewModel: ViewModel, isVisible: Boolean)

    fun setFocused(viewModel: ViewModel, isFocused: Boolean)

}