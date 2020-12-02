package net.apptronic.core.android.viewmodel.bindings.navigation

import net.apptronic.core.android.anim.adapter.BasicViewTransitionAdapter
import net.apptronic.core.android.anim.adapter.ViewTransitionAdapter
import net.apptronic.core.android.anim.adapter.compositeViewTransitionAdapter
import net.apptronic.core.android.plugins.getDefaultViewTransitionAdapterFromPlugin
import net.apptronic.core.android.plugins.getViewBinderAdapterFromExtension
import net.apptronic.core.android.viewmodel.ViewBinderAdapter
import net.apptronic.core.android.viewmodel.composeViewBinderAdapter
import net.apptronic.core.viewmodel.IViewModel

fun getComposedViewBinderAdapter(
    provided: ViewBinderAdapter?,
    viewModel: IViewModel?
): ViewBinderAdapter {
    return composeViewBinderAdapter(
        provided,
        viewModel?.getViewBinderAdapterFromExtension()
    )
}

fun getComposedViewTransitionAdapter(
    provided: ViewTransitionAdapter?,
    viewModel: IViewModel?
): ViewTransitionAdapter {
    return compositeViewTransitionAdapter(
        provided,
        viewModel?.getDefaultViewTransitionAdapterFromPlugin(),
        BasicViewTransitionAdapter
    )
}