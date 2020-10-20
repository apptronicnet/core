package net.apptronic.core.android.viewmodel.navigation

import android.view.LayoutInflater
import android.view.ViewGroup
import net.apptronic.core.android.anim.factory.ViewTransitionFactory
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.view.DefaultViewContainerViewAdapter
import net.apptronic.core.android.viewmodel.view.ViewContainerViewAdapter
import net.apptronic.core.mvvm.viewmodel.navigation.TransitionInfo
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelItem
import net.apptronic.core.mvvm.viewmodel.navigation.adapters.SingleViewModelListAdapter

class SingleViewBinderListAdapter(
    private val container: ViewGroup,
    private val viewBinderFactory: ViewBinderFactory,
    private val viewTransitionFactory: ViewTransitionFactory,
    private val defaultAnimationTime: Long,
    private val maxCachedViews: Int
) : SingleViewModelListAdapter {

    private val layoutInflater = LayoutInflater.from(container.context)

    private val dispatcher = SingleViewContainerDispatcher(
        container, viewTransitionFactory, defaultAnimationTime
    )

    fun bindings(setup: ViewBinderFactory.() -> Unit) {
        setup.invoke(viewBinderFactory)
    }

    private val cachedBinders = mutableListOf<ViewBinder<*>>()
    private var currentBinder: ViewBinder<*>? = null

    companion object {
        const val CACHE_ALL: Int = -1
    }

    override fun onInvalidate(
        items: List<ViewModelItem>, visibleIndex: Int, transitionInfo: TransitionInfo
    ) {
        val oldBinder = currentBinder
        val oldItem = oldBinder?.getItem()
        val newItem = items.getOrNull(visibleIndex)
        if (oldItem?.viewModel == newItem?.viewModel) {
            return
        }
        val newBinder = newItem?.getBinder()
        newBinder?.let {
            cachedBinders.remove(it)
        }
        dispatcher.setView(oldBinder, newBinder, transitionInfo)
        newItem?.setVisible(true)
        newItem?.setFocused(true)
        currentBinder = newBinder
        if (oldItem?.viewModel?.isTerminated() != true) {
            oldItem?.setFocused(false)
            oldItem?.setVisible(false)
        }
        newBinder?.let {
            cachedBinders.add(it)
        }
        clearCache(items)
    }

    private fun ViewModelItem.getBinder(): ViewBinder<*> {
        return cachedBinders.firstOrNull {
            it.getViewModel() == viewModel
        } ?: viewBinderFactory.getBinder(viewModel).also { newBinder ->
            val viewAdapter =
                newBinder as? ViewContainerViewAdapter ?: DefaultViewContainerViewAdapter
            val view = viewAdapter.onCreateView(
                this.viewModel, newBinder, container.context, layoutInflater, container
            )
            this.setBound(true)
            newBinder.performViewBinding(this, view)
        }
    }

    private fun clearCache(items: List<ViewModelItem>) {
        cachedBinders.removeAll {
            val remove = !items.contains(it.getItem())
            if (remove && !it.getViewModel().isTerminated()) {
                it.getItem().setFocused(false)
                it.getItem().setVisible(false)
            }
            remove
        }
        if (maxCachedViews >= 0) {
            while (cachedBinders.size > 0) {
                val first = cachedBinders.removeFirstOrNull()
                if (first != null) {
                    first.getItem().setFocused(false)
                    first.getItem().setVisible(false)
                    first.getItem().setBound(false)
                } else return
            }
        }
    }

}