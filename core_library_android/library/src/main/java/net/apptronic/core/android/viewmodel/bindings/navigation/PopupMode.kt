package net.apptronic.core.android.viewmodel.bindings.navigation

import android.content.Context
import android.view.View
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.navigation.PopupAnchorProvider
import net.apptronic.core.android.viewmodel.navigation.PopupBinderStackAdapter
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigationModel
import net.apptronic.core.mvvm.viewmodel.navigation.models.SupportsSingleViewModelAdapter

private class ConcreteAnchorProvider(val anchor: View) : PopupAnchorProvider {

    override fun provideAnchorForPopup(viewModel: IViewModel) = anchor

}

fun ViewBinder<*>.bindPopupNavigator(
    navigator: StackNavigationModel,
    mode: PopupMode
) {
    bindNavigator(getView(), navigator, mode)
}

fun BindingContainer.bindNavigator(
    container: View,
    navigator: SupportsSingleViewModelAdapter,
    mode: PopupMode
) {
    +PopupNavigatorBinding(container, navigator, mode)
}

class PopupNavigatorBinding(
    private val container: View,
    private val navigator: SupportsSingleViewModelAdapter,
    private val mode: PopupMode
) : Binding() {

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        val binderFactory = getComposedViewBinderFactory(mode.binderFactory, viewModel)
        navigator.setAdapter(
            PopupBinderStackAdapter(mode.context, container, mode.anchorProvider, binderFactory)
        )
    }

}

class PopupMode(
    val context: Context,
    val anchorProvider: PopupAnchorProvider,
    val binderFactory: ViewBinderFactory? = null
)

fun PopupMode(
    anchorView: View,
    binderFactory: ViewBinderFactory? = null
) = PopupMode(
    anchorView.context,
    ConcreteAnchorProvider(anchorView),
    binderFactory
)

fun ViewBinder<*>.PopupMode(
    anchorProvider: PopupAnchorProvider,
    binderFactory: ViewBinderFactory? = null
) = PopupMode(
    getView().context,
    anchorProvider,
    binderFactory
)

fun ViewBinder<*>.PopupMode(
    anchorView: View,
    binderFactory: ViewBinderFactory? = null
) = PopupMode(
    getView().context,
    ConcreteAnchorProvider(anchorView),
    binderFactory
)