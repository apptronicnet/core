package net.apptronic.core.android.viewmodel.bindings.navigation

import android.content.Context
import android.view.View
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderAdapter
import net.apptronic.core.android.viewmodel.navigation.PopupAnchorProvider
import net.apptronic.core.android.viewmodel.navigation.PopupBinderStackAdapter
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.navigation.StackNavigationModel
import net.apptronic.core.viewmodel.navigation.models.SupportsSingleViewModelAdapter

private class ConcreteAnchorProvider(val anchor: View) : PopupAnchorProvider {

    override fun provideAnchorForPopup(viewModel: IViewModel) = anchor

}

fun ViewBinder<*>.bindPopupNavigator(
    navigator: StackNavigationModel,
    mode: PopupMode
) {
    bindNavigator(view, navigator, mode)
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
        val binderFactory = getComposedViewBinderFactory(mode.binderAdapter, viewModel)
        navigator.setAdapter(
            PopupBinderStackAdapter(mode.context, container, mode.anchorProvider, binderFactory)
        )
    }

}

class PopupMode(
    val context: Context,
    val anchorProvider: PopupAnchorProvider,
    val binderAdapter: ViewBinderAdapter? = null
)

fun PopupMode(
    anchorView: View,
    binderAdapter: ViewBinderAdapter? = null
) = PopupMode(
    anchorView.context,
    ConcreteAnchorProvider(anchorView),
    binderAdapter
)

fun ViewBinder<*>.PopupMode(
    anchorProvider: PopupAnchorProvider,
    binderAdapter: ViewBinderAdapter? = null
) = PopupMode(
    view.context,
    anchorProvider,
    binderAdapter
)

fun ViewBinder<*>.PopupMode(
    anchorView: View,
    binderAdapter: ViewBinderAdapter? = null
) = PopupMode(
    view.context,
    ConcreteAnchorProvider(anchorView),
    binderAdapter
)