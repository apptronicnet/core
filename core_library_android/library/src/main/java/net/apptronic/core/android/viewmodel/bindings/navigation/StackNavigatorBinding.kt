package net.apptronic.core.android.viewmodel.bindings.navigation

import android.view.ViewGroup
import net.apptronic.core.android.plugins.getAndroidViewFactoryFromExtension
import net.apptronic.core.android.viewmodel.*
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigator

fun BindingContainer.bindStackNavigator(
    viewGroup: ViewGroup,
    navigator: StackNavigator,
    factory: AndroidViewFactory? = null,
    stackAnimator: StackAnimator = StackAnimator(),
    defaultAnimationTime: Long = viewGroup.resources.getInteger(android.R.integer.config_mediumAnimTime)
        .toLong()
) {
    val resultFactory = factory
        ?: navigator.parent.getAndroidViewFactoryFromExtension()
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
    private val factory: AndroidViewFactory,
    private val stackAnimator: StackAnimator,
    private val defaultAnimationTime: Long
) : Binding() {

    override fun onBind(viewModel: ViewModel, androidView: AndroidView<*>) {
        navigator.setAdapter(
            AndroidViewModelStackAdapter(
                viewGroup,
                factory,
                stackAnimator,
                defaultAnimationTime
            )
        )
    }

}