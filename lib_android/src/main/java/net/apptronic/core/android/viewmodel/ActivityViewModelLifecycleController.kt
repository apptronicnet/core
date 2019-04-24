package net.apptronic.core.android.viewmodel

import android.view.View
import net.apptronic.core.mvvm.viewmodel.container.ViewModelLifecycleController

class ActivityViewModelLifecycleController(
    private val activityView: AndroidActivityView<*>,
    private val contentViewProvider: () -> View
) :
    ViewModelLifecycleController(activityView.viewModel) {

    init {
        activityView.viewModel.doOnBind {
            activityView.bindView(contentViewProvider.invoke())
        }
    }

}