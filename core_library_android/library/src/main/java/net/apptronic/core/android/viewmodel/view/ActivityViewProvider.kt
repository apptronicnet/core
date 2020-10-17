package net.apptronic.core.android.viewmodel.view

import android.app.Activity
import android.view.View
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.mvvm.viewmodel.IViewModel

interface ActivityViewProvider : ViewAdapter {

    /**
     * Create [View] for [activity]
     */
    fun onCreateActivityView(
        viewModel: IViewModel,
        viewBinder: ViewBinder<*>,
        activity: Activity
    ): View {
        return onCreateView(viewBinder, activity, activity.layoutInflater, null)
    }

    /**
     * Set [View] to [activity]
     */
    fun onAttachActivityView(
        viewModel: IViewModel,
        viewBinder: ViewBinder<*>,
        activity: Activity,
        view: View
    ) {
        activity.setContentView(view)
    }

}