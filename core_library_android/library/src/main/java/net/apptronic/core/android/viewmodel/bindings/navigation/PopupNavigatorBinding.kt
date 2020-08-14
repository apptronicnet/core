package net.apptronic.core.android.viewmodel.bindings.navigation

import android.content.Context
import android.view.View
import net.apptronic.core.android.plugins.getViewBinderFactoryFromExtension
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.navigation.PopupAnchorProvider
import net.apptronic.core.android.viewmodel.navigation.PopupBinderStackAdapter
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigator

private class ConcreteAnchorProvider(val anchor: View) : PopupAnchorProvider {

    override fun provideAnchorForPopup(viewModel: ViewModel) = anchor

}

fun ViewBinder<*>.bindPopupNavigator(
    navigator: StackNavigator,
    anchorProvider: PopupAnchorProvider,
    container: View = getView(),
    factory: ViewBinderFactory? = null
) {
    bindPopupNavigator(getView().context, navigator, container, anchorProvider, factory)
}

fun ViewBinder<*>.bindPopupNavigator(
    navigator: StackNavigator,
    anchor: View,
    container: View = getView(),
    factory: ViewBinderFactory? = null
) {
    bindPopupNavigator(getView().context, navigator, container, anchor, factory)
}

fun BindingContainer.bindPopupNavigator(
    context: Context,
    navigator: StackNavigator,
    container: View,
    anchor: View,
    factory: ViewBinderFactory? = null
) = bindPopupNavigator(context, navigator, container, ConcreteAnchorProvider(anchor), factory)

fun BindingContainer.bindPopupNavigator(
    context: Context,
    navigator: StackNavigator,
    container: View,
    anchorProvider: PopupAnchorProvider,
    factory: ViewBinderFactory? = null
) {
    val resultFactory = factory
        ?: navigator.parent.getViewBinderFactoryFromExtension()
        ?: throw IllegalArgumentException("ViewBinderFactory should be provided by parameters or Context.installViewFactoryPlugin()")
    +PopupNavigatorBinding(context, container, anchorProvider, navigator, resultFactory)
}

class PopupNavigatorBinding(
    private val context: Context,
    private val container: View,
    private val anchorProvider: PopupAnchorProvider,
    private val navigator: StackNavigator,
    private val factory: ViewBinderFactory
) : Binding() {

    override fun onBind(viewModel: ViewModel, viewBinder: ViewBinder<*>) {
        navigator.setAdapter(
            PopupBinderStackAdapter(context, container, anchorProvider, factory)
        )
    }

}