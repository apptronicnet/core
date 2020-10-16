package net.apptronic.core.android.viewmodel.bindings.navigation

import android.content.Context
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.navigation.DialogBinderStackAdapter
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigationModel

fun ViewBinder<*>.bindNavigator(
    navigator: StackNavigationModel,
    mode: DialogMode
) {
    bindNavigator(getView().context, navigator, mode)
}

fun BindingContainer.bindNavigator(
    context: Context,
    navigator: StackNavigationModel,
    mode: DialogMode
) {
    +DialogNavigatorBinding(context, navigator, mode)
}

class DialogNavigatorBinding(
    private val context: Context,
    private val navigator: StackNavigationModel,
    private val mode: DialogMode
) : Binding() {

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        val binderFactory = getComposedViewBinderFactory(mode.binderFactory, viewModel)
        navigator.setAdapter(
            DialogBinderStackAdapter(context, binderFactory)
        )
    }

}

class DialogMode(
    val binderFactory: ViewBinderFactory? = null
)