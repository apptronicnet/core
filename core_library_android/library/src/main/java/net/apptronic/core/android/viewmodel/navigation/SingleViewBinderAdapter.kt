package net.apptronic.core.android.viewmodel.navigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.anim.factory.ViewTransitionFactory
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.view.DefaultViewContainerViewAdapter
import net.apptronic.core.android.viewmodel.view.ViewContainerViewAdapter
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.TransitionInfo
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelItem
import net.apptronic.core.mvvm.viewmodel.navigation.adapters.SingleViewModelAdapter

/**
 * Adapter for [StackNavigator]
 *
 * @param container in which [View] should be added
 * @param viewBinderFactory to create [ViewBinder] for [ViewModel]
 * @param viewTransitionFactory for creating animations
 */
class SingleViewBinderAdapter(
    private val container: ViewGroup,
    private val viewBinderFactory: ViewBinderFactory,
    private val viewTransitionFactory: ViewTransitionFactory,
    private val defaultAnimationTime: Long,
) : SingleViewModelAdapter {

    private val layoutInflater = LayoutInflater.from(container.context)

    private val dispatcher = SingleViewContainerDispatcher(
        container, viewTransitionFactory, defaultAnimationTime
    )

    fun bindings(setup: ViewBinderFactory.() -> Unit) {
        setup.invoke(viewBinderFactory)
    }

    private var currentBinder: ViewBinder<*>? = null

    override fun onInvalidate(item: ViewModelItem?, transitionInfo: TransitionInfo) {
        if (item?.viewModel == currentBinder?.getViewModel()) {
            return
        }
        val oldBinder = currentBinder
        val newModel = item?.viewModel
        val newBinder = if (newModel != null) viewBinderFactory.getBinder(newModel) else null
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