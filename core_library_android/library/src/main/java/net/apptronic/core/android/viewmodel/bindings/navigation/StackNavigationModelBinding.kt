package net.apptronic.core.android.viewmodel.bindings.navigation

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.plugins.getViewBinderFactoryFromExtension
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.navigation.BackStackNavigationFrameGestureAdapter
import net.apptronic.core.android.viewmodel.navigation.StackAnimator
import net.apptronic.core.android.viewmodel.navigation.StackNavigationFrameAdapter
import net.apptronic.core.android.viewmodel.navigation.StackNavigationFrameGestureAdapter
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigationViewModel

fun BindingContainer.bindStackNavigator(
    viewGroup: ViewGroup,
    navigationModel: StackNavigationViewModel,
    factory: ViewBinderFactory? = null,
    stackAnimator: StackAnimator = StackAnimator(),
    defaultAnimationTime: Long = viewGroup.resources.getInteger(android.R.integer.config_mediumAnimTime)
        .toLong(),
    gestureAdapter: StackNavigationFrameGestureAdapter? = BackStackNavigationFrameGestureAdapter()
) {
    val resultFactory = factory
        ?: navigationModel.getViewBinderFactoryFromExtension()
        ?: throw IllegalArgumentException("AndroidViewFactory should be provided by parameters or Context.installViewFactoryPlugin()")
    +StackNavigationModelBinding(
        viewGroup,
        navigationModel,
        resultFactory,
        stackAnimator,
        defaultAnimationTime,
        gestureAdapter
    )
}

private class StackNavigationModelBinding(
    private val viewGroup: ViewGroup,
    private val navigationModel: StackNavigationViewModel,
    private val factory: ViewBinderFactory,
    private val stackAnimator: StackAnimator,
    private val defaultAnimationTime: Long,
    private val gestureAdapter: StackNavigationFrameGestureAdapter?
) : Binding(), StackNavigationFrameAdapter.NavigatorAccess {

    override fun getTransition(from: ViewModel?, to: ViewModel?): Any? {
        return navigationModel.getTransitionInfo(from, to)
    }

    override fun onBind(viewModel: ViewModel, viewBinder: ViewBinder<*>) {
        val adapter = StackNavigationFrameAdapter(
            factory, viewGroup, stackAnimator, defaultAnimationTime, navigatorAccess = this
        )
        adapter.bind(navigationModel)
        gestureAdapter?.attach(viewGroup, Target(navigationModel, adapter))
        onUnbind {
            adapter.unbind()
            gestureAdapter?.detach()
        }
    }

    private class Target(
        private val navigationModel: StackNavigationViewModel,
        private val adapter: StackNavigationFrameAdapter
    ) : StackNavigationFrameGestureAdapter.Target {

        override fun getBackView(): View? {
            val previousItem = navigationModel.getSize() - 2
            return if (previousItem >= 0) {
                adapter.getViewAt(previousItem)
            } else null
        }

        override fun getFrontView(): View? {
            val currentItem = navigationModel.getSize() - 1
            return if (currentItem >= 0) {
                adapter.getViewAt(currentItem)
            } else null
        }

        override fun onGestureConfirmedPopBackStack() {
            navigationModel.popBackStack()
        }

        override fun onGestureCancelled() {
            // ignored
        }

    }

}