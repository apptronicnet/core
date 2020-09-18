package net.apptronic.core.android.viewmodel.bindings.navigation

import net.apptronic.core.android.anim.factory.BasicViewTransitionFactory
import net.apptronic.core.android.anim.factory.ViewTransitionFactory
import net.apptronic.core.android.anim.factory.compositeViewTransitionAdapter
import net.apptronic.core.android.plugins.getDefaultViewTransitionAdapterFromPlugin
import net.apptronic.core.mvvm.viewmodel.ViewModel

fun getComposedViewTransitionAdapter(
    provided: ViewTransitionFactory?,
    viewModel: ViewModel?
): ViewTransitionFactory {
    return compositeViewTransitionAdapter(
        provided,
        viewModel?.getDefaultViewTransitionAdapterFromPlugin(),
        BasicViewTransitionFactory
    )
}