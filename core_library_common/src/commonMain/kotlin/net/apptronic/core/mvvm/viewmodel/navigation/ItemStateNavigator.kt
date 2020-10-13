package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.IViewModel

internal interface ItemStateNavigator {

    fun setBound(viewModel: IViewModel, isBound: Boolean)

    fun setVisible(viewModel: IViewModel, isVisible: Boolean)

    fun setFocused(viewModel: IViewModel, isFocused: Boolean)

}