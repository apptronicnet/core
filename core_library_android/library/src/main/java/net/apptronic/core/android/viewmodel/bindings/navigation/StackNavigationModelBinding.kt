package net.apptronic.core.android.viewmodel.bindings.navigation

import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.plugins.getTransitionBuilderFromExtension
import net.apptronic.core.android.plugins.getViewBinderFactoryFromExtension
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.navigation.StackNavigationFrameAdapter
import net.apptronic.core.android.viewmodel.navigation.ViewBinderListAdapter
import net.apptronic.core.android.viewmodel.transitions.GestureDispatcher
import net.apptronic.core.android.viewmodel.transitions.GestureTarget
import net.apptronic.core.android.viewmodel.transitions.NavigationGestureDetector
import net.apptronic.core.android.viewmodel.transitions.TransitionBuilder
import net.apptronic.core.android.viewmodel.transitions.gestures.BackwardTransitionGestureDetector
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.BackNavigationStatus
import net.apptronic.core.mvvm.viewmodel.navigation.StackNavigationViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelItem

fun BindingContainer.bindStackNavigator(
    viewGroup: ViewGroup,
    navigationModel: StackNavigationViewModel,
    factory: ViewBinderFactory? = null,
    transitionBuilder: TransitionBuilder? = null,
    defaultAnimationTime: Long = viewGroup.resources.getInteger(android.R.integer.config_mediumAnimTime)
        .toLong(),
    gestureDetector: NavigationGestureDetector? = BackwardTransitionGestureDetector()
) {
    val resultFactory = factory
        ?: navigationModel.parent.getViewBinderFactoryFromExtension()
        ?: throw IllegalArgumentException("ViewBinderFactory should be provided by parameters or Context.installViewFactoryPlugin()")
    val resultTransitionBuilder = transitionBuilder
        ?: navigationModel.parent.getTransitionBuilderFromExtension()
        ?: TransitionBuilder()
    +StackNavigationModelBinding(
        viewGroup,
        navigationModel,
        resultFactory,
        resultTransitionBuilder,
        defaultAnimationTime,
        gestureDetector
    )
}

private class StackNavigationModelBinding(
    private val viewGroup: ViewGroup,
    private val navigationModel: StackNavigationViewModel,
    private val factory: ViewBinderFactory,
    private val transitionBuilder: TransitionBuilder,
    private val defaultAnimationTime: Long,
    private val gestureDetector: NavigationGestureDetector?
) : Binding() {

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        val listAdapter = ViewBinderListAdapter(factory)
        val adapter = StackNavigationFrameAdapter(
            viewGroup, transitionBuilder, defaultAnimationTime, listAdapter = listAdapter
        )
        adapter.bind(navigationModel)
        if (gestureDetector != null) {
            val gestureDispatcher =
                GestureDispatcher(
                    gestureDetector
                )
            gestureDispatcher.attach(viewGroup, GestureTargetImpl(navigationModel, adapter))
            listAdapter.addListener(object : ViewBinderListAdapter.UpdateListener {
                override fun onDataChanged(items: List<ViewModelItem>, changeInfo: Any?) {
                    gestureDispatcher.reset()
                }
            })
            onUnbind {
                adapter.unbind()
                gestureDispatcher.detach()
            }
        }
    }

    private class GestureTargetImpl(
        private val navigationModel: StackNavigationViewModel,
        private val adapter: StackNavigationFrameAdapter
    ) : GestureTarget {

        override fun getBackNavigationStatus(): BackNavigationStatus {
            if (navigationModel.listNavigator.getSize() <= 1) {
                return BackNavigationStatus.Restricted
            }
            return adapter.getBackNavigationStatus()
        }

        override fun onGestureStarted() {
            getBackView()?.visibility = View.VISIBLE
        }

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
            getFrontView()?.visibility = View.GONE
            adapter.onConfirmBackNavigationFromGesture()
            navigationModel.popBackStack()
        }

        override fun onGestureCancelled(becauseOfRestricted: Boolean) {
            getBackView()?.visibility = View.GONE
            if (becauseOfRestricted) {
                adapter.onRestrictedBackNavigationFromGesture()
            }
        }

    }

}