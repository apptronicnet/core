package net.apptronic.core.android.viewmodel.bindings.navigation

import net.apptronic.core.android.anim.adapter.BasicViewTransitionAdapter
import net.apptronic.core.android.anim.adapter.ViewTransitionAdapter
import net.apptronic.core.android.anim.adapter.compositeViewTransitionAdapter
import net.apptronic.core.android.plugins.getDefaultViewTransitionAdapterFromPlugin
import net.apptronic.core.mvvm.viewmodel.ViewModel

fun getComposedViewTransitionAdapter(
    provided: ViewTransitionAdapter?,
    viewModel: ViewModel?
): ViewTransitionAdapter {
    return compositeViewTransitionAdapter(
        provided,
        viewModel?.getDefaultViewTransitionAdapterFromPlugin(),
        BasicViewTransitionAdapter
    )
}