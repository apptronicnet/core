package net.apptronic.core.android.viewmodel.bindings.navigation

import android.view.ViewGroup
import net.apptronic.core.android.anim.factory.ViewTransitionFactory
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.navigation.SingleViewBinderListAdapter
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.navigation.models.SupportsSingleViewModelListAdapter

fun BindingContainer.bindNavigator(
    container: ViewGroup,
    navigator: SupportsSingleViewModelListAdapter,
    mode: SingleViewListMode
) {
    +SingleViewListModeBinding(container, navigator, mode)
}

private class SingleViewListModeBinding(
    private val container: ViewGroup,
    private val navigator: SupportsSingleViewModelListAdapter,
    private val mode: SingleViewListMode
) : Binding() {

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        val binderFactory = getComposedViewBinderFactory(mode.binderFactory, viewModel)
        val transitionFactory = getComposedViewTransitionFactory(
            mode.transitionFactory, viewModel
        )
        val defaultAnimationTime = mode.defaultAnimationTime
            ?: container.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
        navigator.setAdapter(
            SingleViewBinderListAdapter(
                container,
                binderFactory,
                transitionFactory,
                defaultAnimationTime,
                mode.maxCachedViews
            )
        )
    }

}

class SingleViewListMode(
    val binderFactory: ViewBinderFactory? = null,
    val transitionFactory: ViewTransitionFactory? = null,
    val defaultAnimationTime: Long? = null,
    val maxCachedViews: Int = 5
) {
    companion object {
        const val CACHE_ALL = SingleViewBinderListAdapter.CACHE_ALL
    }
}