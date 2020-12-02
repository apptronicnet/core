package net.apptronic.core.android.viewmodel.navigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.anim.adapter.ViewTransitionAdapter
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderAdapter
import net.apptronic.core.android.viewmodel.view.DefaultViewContainerViewAdapter
import net.apptronic.core.android.viewmodel.view.ViewContainerViewAdapter
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.navigation.TransitionInfo
import net.apptronic.core.viewmodel.navigation.ViewModelItem
import net.apptronic.core.viewmodel.navigation.adapters.SingleViewModelAdapter

/**
 * Adapter for [StackNavigator]
 *
 * @param container in which [View] should be added
 * @param viewBinderAdapter to create [ViewBinder] for [ViewModel]
 * @param viewTransitionAdapter for creating animations
 */
class SingleViewBinderAdapter(
    private val container: ViewGroup,
    private val viewBinderAdapter: ViewBinderAdapter,
    private val viewTransitionAdapter: ViewTransitionAdapter,
    private val defaultAnimationTime: Long,
) : SingleViewModelAdapter {

    private val layoutInflater = LayoutInflater.from(container.context)

    private val dispatcher = SingleViewContainerDispatcher(
        container, viewTransitionAdapter, defaultAnimationTime
    )

    fun bindings(setup: ViewBinderAdapter.() -> Unit) {
        setup.invoke(viewBinderAdapter)
    }

    private var currentBinder: ViewBinder<*>? = null

    override fun onInvalidate(item: ViewModelItem?, transitionInfo: TransitionInfo) {
        if (item?.viewModel == currentBinder?.viewModel) {
            return
        }
        val oldBinder = currentBinder
        val newModel = item?.viewModel
        val newBinder = if (newModel != null) viewBinderAdapter.getBinder(newModel) else null
        if (newBinder != null && newModel != null) {
            val viewAdapter =
                newBinder as? ViewContainerViewAdapter ?: DefaultViewContainerViewAdapter
            val view = viewAdapter.onCreateView(
                newModel, newBinder, container.context, layoutInflater, container
            )
            newBinder.performViewBinding(newModel, view)
        }
        dispatcher.setView(oldBinder, newBinder, transitionInfo)
        currentBinder = newBinder
    }

}