package net.apptronic.core.ios.viewmodel.bindings.navigation

import net.apptronic.core.ios.viewmodel.Binding
import net.apptronic.core.ios.viewmodel.BindingContainer
import net.apptronic.core.ios.viewmodel.ViewBinder
import net.apptronic.core.ios.viewmodel.ViewBinderFactory
import net.apptronic.core.ios.viewmodel.navigation.SingleViewBinderAdapter
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigator
import platform.UIKit.UIView

fun BindingContainer.bindStackNavigator(
        container: UIView,
        navigator: StackNavigator,
        factory: ViewBinderFactory
) {
    +StackNavigatorBinding(
            container,
            navigator,
            factory
    )
}

private class StackNavigatorBinding(
        private val container: UIView,
        private val navigator: StackNavigator,
        private val factory: ViewBinderFactory
) : Binding() {

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*, *>) {
        navigator.setAdapter(
                SingleViewBinderAdapter(
                        container,
                        factory
                )
        )
    }

}