package net.apptronic.core.android.viewmodel.view

import android.app.Activity
import android.view.View
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.mvvm.viewmodel.IViewModel

open class ActivityDelegate<T : IViewModel> {

    @Suppress("UNCHECKED_CAST")
    fun performCreateActivityView(
        viewModel: IViewModel,
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
        return viewBinder.onCreateView(activity, activity.layoutInflater, null)
    }

    @Suppress("UNCHECKED_CAST")
    fun performAttachActivityView(
        viewModel: IViewModel,
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