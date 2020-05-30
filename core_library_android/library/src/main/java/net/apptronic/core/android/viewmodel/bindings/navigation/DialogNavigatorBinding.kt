package net.apptronic.core.android.viewmodel.bindings.navigation

import android.content.Context
import net.apptronic.core.android.plugins.getAndroidViewFactoryFromExtension
import net.apptronic.core.android.viewmodel.*
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigator

fun AndroidView<*>.bindDialogNavigator(
    navigator: StackNavigator,
    factory: AndroidViewFactory? = null
) {
    bindDialogNavigator(getView().context, navigator, factory)
}

fun BindingContainer.bindDialogNavigator(
    context: Context,
    navigator: StackNavigator,
    factory: AndroidViewFactory? = null
) {
    val resultFactory = factory
        ?: navigator.parent.getAndroidViewFactoryFromExtension()
        ?: throw IllegalArgumentException("AndroidViewFactory should be provided by parameters or Context.installViewFactoryPlugin()")
    add(DialogNavigatorBinding(context, navigator, resultFactory))
}

class DialogNavigatorBinding(
    private val context: Context,
    private val navigator: StackNavigator,
    private val factory: AndroidViewFactory
) : Binding() {

    override fun onBind(viewModel: ViewModel, androidView: AndroidView<*>) {
        navigator.setAdapter(DialogViewModelStackAdapter(context, factory))
    }

}