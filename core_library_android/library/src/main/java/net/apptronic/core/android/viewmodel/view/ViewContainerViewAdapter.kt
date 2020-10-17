package net.apptronic.core.android.viewmodel.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel

interface ViewContainerViewAdapter : ViewAdapter {

    /**
     * Invoke to create generic view for parameters without knowledge of concrete [ViewModel]
     * which requires this view (for example for use in lists with many same items)
     */
    fun onCreateDetachedView(
        context: Context,
        viewBinder: ViewBinder<*>,
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        return viewBinder.onCreateView(viewBinder, context, LayoutInflater.from(context), container)
    }

    /**
     * Create [View] for adding to [container]
     */
    fun onCreateView(
        viewModel: IViewModel,
        viewBinder: ViewBinder<*>,
        context: Context,
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        return viewBinder.onCreateView(viewBinder, context, inflater, container)
    }

    /**
     * Attach [view] to a [container] meaning it should be added and, if needed, perform additional
     * actions
     */
    fun onAttachView(viewModel: IViewModel, view: View, container: ViewGroup, position: Int) {
        container.isSaveFromParentEnabled = false
        container.addView(view, position)
    }

    /**
     * Attach [view] to a [container] meaning it should be added and, if needed, perform additional
     * actions
     */
    fun onDetachView(
        viewModel: IViewModel,
        viewBinder: ViewBinder<*>,
        view: View,
        container: ViewGroup
    ) {
        container.isSaveFromParentEnabled = false
        container.removeView(view)
    }

}