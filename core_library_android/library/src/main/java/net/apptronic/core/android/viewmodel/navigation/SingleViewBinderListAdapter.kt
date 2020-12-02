package net.apptronic.core.android.viewmodel.navigation

import android.view.LayoutInflater
import android.view.ViewGroup
import net.apptronic.core.android.anim.adapter.ViewTransitionAdapter
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.ViewBinderAdapter
import net.apptronic.core.android.viewmodel.view.DefaultViewContainerViewAdapter
import net.apptronic.core.android.viewmodel.view.ViewContainerViewAdapter
import net.apptronic.core.viewmodel.navigation.TransitionInfo
import net.apptronic.core.viewmodel.navigation.ViewModelItem
import net.apptronic.core.viewmodel.navigation.adapters.SingleViewModelListAdapter

class SingleViewBinderListAdapter(
    private val container: ViewGroup,
    private val viewBinderAdapter: ViewBinderAdapter,
    private val viewTransitionAdapter: ViewTransitionAdapter,
    private val defaultAnimationTime: Long,
    private val maxCachedViews: Int
) : SingleViewModelListAdapter {

    private val layoutInflater = LayoutInflater.from(container.context)

    private val dispatcher = SingleViewContainerDispatcher(
        container, viewTransitionAdapter, defaultAnimationTime
    )

    fun bindings(setup: ViewBinderAdapter.() -> Unit) {
        setup.invoke(viewBinderAdapter)
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
        val oldItem = oldBinder?.viewModelItem
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
        if (oldItem != null && !oldItem.viewModel.isTerminated()) {
            oldItem.setFocused(false)
            oldItem.setVisible(false)
            cachedBinders.add(oldBinder)
        }
        clearCache(items)
    }

    private fun ViewModelItem.getBinder(): ViewBinder<*> {
        return cachedBinders.firstOrNull {
            it.viewModel == viewModel
        } ?: viewBinderAdapter.getBinder(viewModel).also { newBinder ->
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
            val remove = !items.contains(it.viewModelItem)
            if (remove && !it.viewModel.isTerminated()) {
                it.viewModelItem.setFocused(false)
                it.viewModelItem.setVisible(false)
            }
            remove
        }
        if (maxCachedViews >= 0) {
            while (cachedBinders.size > maxCachedViews) {
                val first = cachedBinders.removeFirstOrNull()
                if (first != null) {
                    first.viewModelItem.setFocused(false)
                    first.viewModelItem.setVisible(false)
                    first.viewModelItem.setBound(false)
                } else return
            }
        }
    }

}