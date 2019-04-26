package net.apptronic.core.android.viewmodel

import android.view.View
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.container.ViewModelLifecycleController

class ActivityViewModelLifecycleController<T : ViewModel>(
    private val activityView: AndroidView<T>,
    private val viewModel: T,
    private val contentViewProvider: () -> View
) : ViewModelLifecycleController(viewModel) {

    init {
        viewModel.doOnBind {
            activityView.requestBinding(contentViewProvider.invoke(), viewModel)
        }
    }

}