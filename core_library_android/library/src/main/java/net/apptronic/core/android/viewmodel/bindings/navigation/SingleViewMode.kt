package net.apptronic.core.android.viewmodel.bindings.navigation

import android.view.ViewGroup
import net.apptronic.core.android.anim.adapter.ViewTransitionAdapter
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderAdapter
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
        val binderAdapter = getComposedViewBinderAdapter(mode.binderAdapter, viewModel)
        val transitionAdapter = getComposedViewTransitionAdapter(
            mode.transitionAdapter, viewModel
        )
        val defaultAnimationTime = mode.defaultAnimationTime
            ?: container.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
        navigator.setAdapter(
            SingleViewBinderAdapter(
                container, binderAdapter, transitionAdapter, defaultAnimationTime
            )
        )
    }

}

class SingleViewMode(
    val binderAdapter: ViewBinderAdapter? = null,
    val transitionAdapter: ViewTransitionAdapter? = null,
    val defaultAnimationTime: Long? = null
)