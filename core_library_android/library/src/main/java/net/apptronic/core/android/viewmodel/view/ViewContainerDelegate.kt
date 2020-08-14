package net.apptronic.core.android.viewmodel.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.mvvm.viewmodel.ViewModel

open class ViewContainerDelegate<T : ViewModel> {

    /**
     * Invoke to create generic view for parameters without knowledge of concrete [ViewModel]
     * which requires this view (for example for use in lists with many same items)
     */
    @Suppress("UNCHECKED_CAST")
    fun performCreateDetachedView(
        context: Context,
        viewBinder: ViewBinder<*>,
        inflater: LayoutInflater? = null,
        container: ViewGroup? = null
    ): View {
        val realInflater = inflater ?: LayoutInflater.from(context)
        return onCreateDetachedView(context, viewBinder as ViewBinder<T>, realInflater, container)
    }

    /**
     * Create [View] for adding to [container]
     */
    open fun onCreateDetachedView(
        context: Context,
        viewBinder: ViewBinder<T>,
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        val layoutResId = viewBinder.layoutResId
            ?: throw IllegalStateException("[layoutResId] is not specified for $viewBinder")
        return inflater.inflate(layoutResId, container, false)
    }

    @Suppress("UNCHECKED_CAST")
    fun performCreateView(
        viewModel: ViewModel,
        viewBinder: ViewBinder<*>,
        context: Context,
        inflater: LayoutInflater? = null,
        container: ViewGroup? = null
    ): View {
        val realInflater = inflater ?: LayoutInflater.from(context)
        return onCreateView(
            viewModel as T,
            viewBinder as ViewBinder<T>,
            context,
            realInflater,
            container
        )
    }

    /**
     * Create [View] for adding to [container]
     */
    protected open fun onCreateView(
        viewModel: T,
        viewBinder: ViewBinder<T>,
        context: Context,
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        val layoutResId = viewBinder.layoutResId
            ?: throw IllegalStateException("[layoutResId] is not specified for $viewBinder")
        return inflater.inflate(layoutResId, container, false)
    }

    @Suppress("UNCHECKED_CAST")
    fun performAttachView(
        viewModel: ViewModel,
        view: View,
        container: ViewGroup,
        position: Int = container.childCount
    ) {
        onAttachView(viewModel as T, view, container, position)
    }

    /**
     * Attach [view] to a [container] meaning it should be added and, if needed, perform additional
     * actions
     */
    protected open fun onAttachView(viewModel: T, view: View, container: ViewGroup, position: Int) {
        container.isSaveFromParentEnabled = false
        container.addView(view, position)
    }

    @Suppress("UNCHECKED_CAST")
    fun performDetachView(
        viewModel: ViewModel,
        viewBinder: ViewBinder<*>,
        view: View,
        container: ViewGroup
    ) {
        onDetachView(viewModel as T, viewBinder as ViewBinder<T>, view, container)
    }

    /**
     * Attach [view] to a [container] meaning it should be added and, if needed, perform additional
     * actions
     */
    protected open fun onDetachView(
        viewModel: T,
        viewBinder: ViewBinder<T>,
        view: View,
        container: ViewGroup
    ) {
        container.isSaveFromParentEnabled = false
        container.removeView(view)
    }

}