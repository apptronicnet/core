package net.apptronic.core.android.viewmodel.bindings.navigation

import android.view.ViewGroup
import net.apptronic.core.android.anim.factory.ViewTransitionFactory
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.navigation.SingleViewBinderAdapter
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.navigation.models.SupportsSingleViewModelAdapter

fun BindingContainer.bindNavigator(
    container: ViewGroup,
    navigator: SupportsSingleViewModelAdapter,
    mode: SingleViewMode
) {
    +SingleViewModelBinding(container, navigator, mode)
}

private class SingleViewModelBinding(
    private val container: ViewGroup,
    private val navigator: SupportsSingleViewModelAdapter,
    private val mode: SingleViewMode
) : Binding() {

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        val binderFactory = getComposedViewBinderFactory(mode.binderFactory, viewModel)
        val transitionFactory = getComposedViewTransitionFactory(
            mode.transitionFactory, viewModel
        )
        val defaultAnimationTime = mode.defaultAnimationTime
            ?: container.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
        navigator.setAdapter(
            SingleViewBinderAdapter(
                container, binderFactory, transitionFactory, defaultAnimationTime
            )
        )
    }

}

class SingleViewMode(
    val binderFactory: ViewBinderFactory? = null,
    val transitionFactory: ViewTransitionFactory? = null,
    val defaultAnimationTime: Long? = null
)