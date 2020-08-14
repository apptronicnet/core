package net.apptronic.core.android.viewmodel.view

import android.app.Activity
import android.view.View
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.mvvm.viewmodel.ViewModel

open class ActivityDelegate<T : ViewModel> {

    @Suppress("UNCHECKED_CAST")
    fun performCreateActivityView(
        viewModel: ViewModel,
        viewBinder: ViewBinder<*>,
        activity: Activity
    ): View {
        return onCreateActivityView(viewModel as T, viewBinder as ViewBinder<T>, activity)
    }

    /**
     * Create [View] for [activity]
     */
    protected open fun onCreateActivityView(
        viewModel: T,
        viewBinder: ViewBinder<T>,
        activity: Activity
    ): View {
        val layoutResId = viewBinder.layoutResId
            ?: throw IllegalStateException("[layoutResId] is not specified for $viewBinder")
        return activity.layoutInflater.inflate(layoutResId, null, false)
    }

    @Suppress("UNCHECKED_CAST")
    fun performAttachActivityView(
        viewModel: ViewModel,
        viewBinder: ViewBinder<*>,
        activity: Activity,
        view: View
    ) {
        onAttachActivityView(viewModel as T, viewBinder as ViewBinder<T>, activity, view)
    }

    /**
     * Set [View] to [activity]
     */
    protected open fun onAttachActivityView(
        viewModel: T,
        viewBinder: ViewBinder<T>,
        activity: Activity,
        view: View
    ) {
        activity.setContentView(view)
    }


}