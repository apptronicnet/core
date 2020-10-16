package net.apptronic.core.android.viewmodel.bindings.navigation

import net.apptronic.core.android.anim.factory.BasicViewTransitionFactory
import net.apptronic.core.android.anim.factory.ViewTransitionFactory
import net.apptronic.core.android.anim.factory.compositeViewTransitionFactory
import net.apptronic.core.android.plugins.getDefaultViewTransitionFactoryFromPlugin
import net.apptronic.core.android.plugins.getViewBinderFactoryFromExtension
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.composeViewBinderFactory
import net.apptronic.core.mvvm.viewmodel.IViewModel

fun getComposedViewBinderFactory(
    provided: ViewBinderFactory?,
    viewModel: IViewModel?
): ViewBinderFactory {
    return composeViewBinderFactory(
        provided,
        viewModel?.getViewBinderFactoryFromExtension()
    )
}

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