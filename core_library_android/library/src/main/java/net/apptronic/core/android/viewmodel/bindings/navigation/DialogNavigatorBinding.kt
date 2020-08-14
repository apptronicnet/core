package net.apptronic.core.android.viewmodel.bindings.navigation

import android.content.Context
import net.apptronic.core.android.plugins.getViewBinderFactoryFromExtension
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.navigation.DialogBinderStackAdapter
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigator

fun ViewBinder<*>.bindDialogNavigator(
    navigator: StackNavigator,
    factory: ViewBinderFactory? = null
) {
    bindDialogNavigator(getView().context, navigator, factory)
}

fun BindingContainer.bindDialogNavigator(
    context: Context,
    navigator: StackNavigator,
    factory: ViewBinderFactory? = null
) {
    val resultFactory = factory
        ?: navigator.parent.getViewBinderFactoryFromExtension()
        ?: throw IllegalArgumentException("ViewBinderFactory should be provided by parameters or Context.installViewFactoryPlugin()")
    +DialogNavigatorBinding(context, navigator, resultFactory)
}

class DialogNavigatorBinding(
    private val context: Context,
    private val navigator: StackNavigator,
    private val factory: ViewBinderFactory
) : Binding() {

    override fun onBind(viewModel: ViewModel, viewBinder: ViewBinder<*>) {
        navigator.setAdapter(
            DialogBinderStackAdapter(context, factory)
        )
    }

}