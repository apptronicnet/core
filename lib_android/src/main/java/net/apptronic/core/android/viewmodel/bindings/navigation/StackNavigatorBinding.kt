package net.apptronic.core.android.viewmodel.bindings.navigation

import android.view.ViewGroup
import net.apptronic.core.android.viewmodel.AndroidViewFactory
import net.apptronic.core.android.viewmodel.AndroidViewModelStackAdapter
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.mvvm.viewmodel.container.ViewModelStackNavigator

fun stackNavigatorBinding(
    viewGroup: ViewGroup,
    navigator: ViewModelStackNavigator,
    factory: AndroidViewFactory
): StackNavigatorBinding {
    return StackNavigatorBinding(viewGroup, navigator, factory)
}

class StackNavigatorBinding(
    private val viewGroup: ViewGroup,
    private val navigator: ViewModelStackNavigator,
    private val factory: AndroidViewFactory
) : Binding() {

    override fun onBind() {
        navigator.setAdapter(AndroidViewModelStackAdapter(viewGroup, factory))
    }

}