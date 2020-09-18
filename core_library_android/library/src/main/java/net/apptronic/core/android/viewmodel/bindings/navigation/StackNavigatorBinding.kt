package net.apptronic.core.android.viewmodel.bindings.navigation

import android.view.ViewGroup
import net.apptronic.core.android.anim.factory.ViewTransitionFactory
import net.apptronic.core.android.plugins.getViewBinderFactoryFromExtension
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.navigation.ViewBinderStackAdapter
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigator

fun BindingContainer.bindStackNavigator(
    viewGroup: ViewGroup,
    navigator: StackNavigator,
    factory: ViewBinderFactory? = null,
    transitionFactory: ViewTransitionFactory? = null,
    defaultAnimationTime: Long = viewGroup.resources.getInteger(android.R.integer.config_mediumAnimTime)
        .toLong()
) {
    val resultFactory = factory
        ?: navigator.parent.getViewBinderFactoryFromExtension()
        ?: throw IllegalArgumentException("ViewBinderFactory should be provided by parameters or Context.installViewFactoryPlugin()")
    val resultTransitionAdapter = getComposedViewTransitionFactory(
        transitionFactory, navigator.parent
    )
    +StackNavigatorBinding(
        viewGroup,
        navigator,
        resultFactory,
        resultTransitionAdapter,
        defaultAnimationTime
    )
}

private class StackNavigatorBinding(
    private val viewGroup: ViewGroup,
    private val navigator: StackNavigator,
    private val factory: ViewBinderFactory,
    private val viewTransitionFactory: ViewTransitionFactory,
    private val defaultAnimationTime: Long
) : Binding() {

    override fun onBind(viewModel: ViewModel, viewBinder: ViewBinder<*>) {
        navigator.setAdapter(
            ViewBinderStackAdapter(
                viewGroup,
                factory,
                viewTransitionFactory,
                defaultAnimationTime
            )
        )
    }

}