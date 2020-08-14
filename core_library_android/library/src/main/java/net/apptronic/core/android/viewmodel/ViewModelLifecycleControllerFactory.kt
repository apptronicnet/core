package net.apptronic.core.android.viewmodel

import android.app.Activity
import android.view.View
import net.apptronic.core.android.viewmodel.view.ActivityDelegate
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelLifecycleController

fun <T : ViewModel> lifecycleController(
    viewModel: T,
    activityBinder: ViewBinder<T>,
    activity: Activity
): ViewModelLifecycleController {
    viewModel.doOnBind {
        val delegate = activityBinder.getViewDelegate<ActivityDelegate<*>>()
        val view = delegate.performCreateActivityView(viewModel, activityBinder, activity)
        delegate.performAttachActivityView(viewModel, activityBinder, activity, view)
        activityBinder.performViewBinding(viewModel, view)
    }
    return ViewModelLifecycleController(viewModel)
}

fun <T : ViewModel> lifecycleController(
    viewModel: T,
    factory: ViewBinderFactory,
    activity: Activity
): ViewModelLifecycleController {
    val binder = factory.getBinder(viewModel) as ViewBinder<T>
    return lifecycleController(viewModel, binder, activity)
}

fun <T : ViewModel> lifecycleController(
    viewModel: T,
    viewBinder: ViewBinder<T>,
    contentViewProvider: () -> View
): ViewModelLifecycleController {
    viewModel.doOnBind {
        viewBinder.performViewBinding(viewModel, contentViewProvider.invoke())
    }
    return ViewModelLifecycleController(viewModel)
}

fun <T : ViewModel> lifecycleController(
    viewModel: T,
    factory: ViewBinderFactory,
    contentViewProvider: () -> View
): ViewModelLifecycleController {
    val binder = factory.getBinder(viewModel) as ViewBinder<T>
    return lifecycleController(viewModel, binder, contentViewProvider)
}


fun <T : ViewModel> lifecycleController(
    viewModel: T,
    viewBinder: ViewBinder<T>,
    contentView: View
): ViewModelLifecycleController {
    viewModel.doOnBind {
        viewBinder.performViewBinding(viewModel, contentView)
    }
    return ViewModelLifecycleController(viewModel)
}

fun <T : ViewModel> lifecycleController(
    viewModel: T,
    factory: ViewBinderFactory,
    contentView: View
): ViewModelLifecycleController {
    val binder = factory.getBinder(viewModel) as ViewBinder<T>
    return lifecycleController(viewModel, binder, contentView)
}