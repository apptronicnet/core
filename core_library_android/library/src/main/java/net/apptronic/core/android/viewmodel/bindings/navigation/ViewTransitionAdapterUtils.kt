package net.apptronic.core.android.viewmodel.bindings.navigation

import net.apptronic.core.android.anim.factory.BasicViewTransitionFactory
import net.apptronic.core.android.anim.factory.ViewTransitionFactory
import net.apptronic.core.android.anim.factory.compositeViewTransitionFactory
import net.apptronic.core.android.plugins.getDefaultViewTransitionFactoryFromPlugin
import net.apptronic.core.mvvm.viewmodel.IViewModel

fun getComposedViewTransitionFactory(
    provided: ViewTransitionFactory?,
    viewModel: IViewModel?
): ViewTransitionFactory {
    return compositeViewTransitionFactory(
        provided,
        viewModel?.getDefaultViewTransitionFactoryFromPlugin(),
        BasicViewTransitionFactory
    )
}