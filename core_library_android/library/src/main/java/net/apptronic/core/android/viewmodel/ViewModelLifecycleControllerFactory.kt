package net.apptronic.core.android.viewmodel

import android.app.Activity
import android.view.View
import net.apptronic.core.android.viewmodel.view.ActivityViewProvider
import net.apptronic.core.android.viewmodel.view.DefaultActivityViewProvider
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.navigation.ViewModelLifecycleController

fun <T : IViewModel> lifecycleController(
    viewModel: T,
    activityBinder: ViewBinder<T>,
    activity: Activity
): ViewModelLifecycleController {
    viewModel.doOnBind {
        val viewProvider = activityBinder as? ActivityViewProvider ?: DefaultActivityViewProvider
        val view = viewProvider.onCreateActivityView(viewModel, activityBinder, activity)
        viewProvider.onAttachActivityView(viewModel, activityBinder, activity, view)
        activityBinder.performViewBinding(viewModel, view)
    }
    return ViewModelLifecycleController(viewModel)
}

fun <T : IViewModel> lifecycleController(
    viewModel: T,
    adapter: ViewBinderAdapter,
    activity: Activity
): ViewModelLifecycleController {
    @Suppress("UNCHECKED_CAST")
    val binder = adapter.getBinder(viewModel)
    return lifecycleController(viewModel, binder, activity)
}

fun <T : IViewModel> lifecycleController(
    viewModel: T,
    viewBinder: ViewBinder<T>,
    contentViewProvider: () -> View
): ViewModelLifecycleController {
    viewModel.doOnBind {
        viewBinder.performViewBinding(viewModel, contentViewProvider.invoke())
    }
    return ViewModelLifecycleController(viewModel)
}

fun <T : IViewModel> lifecycleController(
    viewModel: T,
    adapter: ViewBinderAdapter,
    contentViewProvider: () -> View
): ViewModelLifecycleController {
    @Suppress("UNCHECKED_CAST")
    val binder = adapter.getBinder(viewModel)
    return lifecycleController(viewModel, binder, contentViewProvider)
}


fun <T : IViewModel> lifecycleController(
    viewModel: T,
    viewBinder: ViewBinder<T>,
    contentView: View
): ViewModelLifecycleController {
    viewModel.doOnBind {
        viewBinder.performViewBinding(viewModel, contentView)
    }
    return ViewModelLifecycleController(viewModel)
}

fun <T : IViewModel> lifecycleController(
    viewModel: T,
    adapter: ViewBinderAdapter,
    contentView: View
): ViewModelLifecycleController {
    @Suppress("UNCHECKED_CAST")
    val binder = adapter.getBinder(viewModel)
    return lifecycleController(viewModel, binder, contentView)
}