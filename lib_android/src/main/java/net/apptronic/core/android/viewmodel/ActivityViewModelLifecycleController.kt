package net.apptronic.core.android.viewmodel

import android.app.Activity
import android.view.View
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.container.ViewModelLifecycleController

fun <T : ViewModel> activityLifecycleController(
    viewModel: T,
    activityView: AndroidView<T>,
    activity: Activity
): ViewModelLifecycleController {
    viewModel.doOnBind {
        val view = activityView.onCreateActivityView(activity)
        activity.setContentView(view)
        activityView.bindView(view, viewModel)
    }
    return ViewModelLifecycleController(viewModel)
}

fun <T : ViewModel> activityLifecycleController(
    viewModel: T,
    factory: AndroidViewFactory,
    activity: Activity
): ViewModelLifecycleController {
    val androidView = factory.getAndroidView(viewModel) as AndroidView<T>
    return activityLifecycleController(viewModel, androidView, activity)
}

fun <T : ViewModel> activityLifecycleController(
    viewModel: T,
    activityView: AndroidView<T>,
    contentViewProvider: () -> View
): ViewModelLifecycleController {
    viewModel.doOnBind {
        activityView.bindView(contentViewProvider.invoke(), viewModel)
    }
    return ViewModelLifecycleController(viewModel)
}

fun <T : ViewModel> activityLifecycleController(
    viewModel: T,
    factory: AndroidViewFactory,
    contentViewProvider: () -> View
): ViewModelLifecycleController {
    val androidView = factory.getAndroidView(viewModel) as AndroidView<T>
    return activityLifecycleController(viewModel, androidView, contentViewProvider)
}


fun <T : ViewModel> activityLifecycleController(
    viewModel: T,
    activityView: AndroidView<T>,
    contentView: View
): ViewModelLifecycleController {
    viewModel.doOnBind {
        activityView.bindView(contentView, viewModel)
    }
    return ViewModelLifecycleController(viewModel)
}

fun <T : ViewModel> activityLifecycleController(
    viewModel: T,
    factory: AndroidViewFactory,
    contentView: View
): ViewModelLifecycleController {
    val androidView = factory.getAndroidView(viewModel) as AndroidView<T>
    return activityLifecycleController(viewModel, androidView, contentView)
}