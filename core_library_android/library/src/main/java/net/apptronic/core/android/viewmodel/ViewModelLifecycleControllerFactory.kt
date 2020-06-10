package net.apptronic.core.android.viewmodel

import android.app.Activity
import android.view.View
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelLifecycleController

fun <T : ViewModel> lifecycleController(
    viewModel: T,
    activityBinder: ViewBinder<T>,
    activity: Activity
): ViewModelLifecycleController {
    viewModel.doOnBind {
        val view = activityBinder.onCreateActivityView(activity)
        activity.setContentView(view)
        activityBinder.bindView(view, viewModel)
    }
    return ViewModelLifecycleController(viewModel)
}

fun <T : ViewModel> lifecycleController(
    viewModel: T,
    factory: ViewBinderFactory,
    activity: Activity
): ViewModelLifecycleController {
    val androidView = factory.getBinder(viewModel) as ViewBinder<T>
    return lifecycleController(viewModel, androidView, activity)
}

fun <T : ViewModel> lifecycleController(
    viewModel: T,
    viewBinder: ViewBinder<T>,
    contentViewProvider: () -> View
): ViewModelLifecycleController {
    viewModel.doOnBind {
        viewBinder.bindView(contentViewProvider.invoke(), viewModel)
    }
    return ViewModelLifecycleController(viewModel)
}

fun <T : ViewModel> lifecycleController(
    viewModel: T,
    factory: ViewBinderFactory,
    contentViewProvider: () -> View
): ViewModelLifecycleController {
    val androidView = factory.getBinder(viewModel) as ViewBinder<T>
    return lifecycleController(viewModel, androidView, contentViewProvider)
}


fun <T : ViewModel> lifecycleController(
    viewModel: T,
    viewBinder: ViewBinder<T>,
    contentView: View
): ViewModelLifecycleController {
    viewModel.doOnBind {
        viewBinder.bindView(contentView, viewModel)
    }
    return ViewModelLifecycleController(viewModel)
}

fun <T : ViewModel> lifecycleController(
    viewModel: T,
    factory: ViewBinderFactory,
    contentView: View
): ViewModelLifecycleController {
    val androidView = factory.getBinder(viewModel) as ViewBinder<T>
    return lifecycleController(viewModel, androidView, contentView)
}