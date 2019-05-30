package net.apptronic.core.android.viewmodel.bindings.navigation

import android.view.ViewGroup
import net.apptronic.core.android.viewmodel.*
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.container.ViewModelStackNavigator

fun stackNavigatorBinding(
        viewGroup: ViewGroup,
        navigator: ViewModelStackNavigator,
        factory: AndroidViewFactory,
        stackAnimator: StackAnimator = StackAnimator(),
        defaultAnimationTime: Long = viewGroup.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
): StackNavigatorBinding {
    return StackNavigatorBinding(viewGroup, navigator, factory, stackAnimator, defaultAnimationTime)
}

class StackNavigatorBinding(
        private val viewGroup: ViewGroup,
        private val navigator: ViewModelStackNavigator,
        private val factory: AndroidViewFactory,
        private val stackAnimator: StackAnimator,
        private val defaultAnimationTime: Long
) : Binding() {

    override fun onBind(viewModel: ViewModel, androidView: AndroidView<*>) {
        navigator.setAdapter(AndroidViewModelStackAdapter(viewGroup, factory, stackAnimator, defaultAnimationTime))
    }

}