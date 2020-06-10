package net.apptronic.core.android.viewmodel.bindings.navigation

import android.view.ViewGroup
import net.apptronic.core.android.plugins.getViewBinderFactoryFromExtension
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.navigation.StackAnimator
import net.apptronic.core.android.viewmodel.navigation.ViewBinderStackAdapter
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigator

fun BindingContainer.bindStackNavigator(
    viewGroup: ViewGroup,
    navigator: StackNavigator,
    factory: ViewBinderFactory? = null,
    stackAnimator: StackAnimator = StackAnimator(),
    defaultAnimationTime: Long = viewGroup.resources.getInteger(android.R.integer.config_mediumAnimTime)
        .toLong()
) {
    val resultFactory = factory
        ?: navigator.parent.getViewBinderFactoryFromExtension()
        ?: throw IllegalArgumentException("AndroidViewFactory should be provided by parameters or Context.installViewFactoryPlugin()")
    add(
        StackNavigatorBinding(
            viewGroup,
            navigator,
            resultFactory,
            stackAnimator,
            defaultAnimationTime
        )
    )
}

private class StackNavigatorBinding(
    private val viewGroup: ViewGroup,
    private val navigator: StackNavigator,
    private val factory: ViewBinderFactory,
    private val stackAnimator: StackAnimator,
    private val defaultAnimationTime: Long
) : Binding() {

    override fun onBind(viewModel: ViewModel, viewBinder: ViewBinder<*>) {
        navigator.setAdapter(
            ViewBinderStackAdapter(
                viewGroup,
                factory,
                stackAnimator,
                defaultAnimationTime
            )
        )
    }

}