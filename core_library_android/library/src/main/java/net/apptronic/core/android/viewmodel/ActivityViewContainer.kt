package net.apptronic.core.android.viewmodel

import android.app.Activity
import net.apptronic.core.android.plugins.getViewBinderAdapterFromExtension
import net.apptronic.core.android.viewmodel.view.ActivityViewProvider
import net.apptronic.core.android.viewmodel.view.DefaultActivityViewProvider
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.dispatcher.ViewContainer
import net.apptronic.core.viewmodel.dispatcher.ViewModelDispatcher
import net.apptronic.core.viewmodel.navigation.ViewModelLifecycleController

fun <T : IViewModel> Activity.activityContainer(
    dispatcher: ViewModelDispatcher<T>,
    activityBinder: ViewBinder<T>
): ActivityViewContainer<T> {
    return ActivityViewContainerImpl<T>(
        this, dispatcher
    ) {
        val viewProvider = activityBinder as? ActivityViewProvider ?: DefaultActivityViewProvider
        val view = viewProvider.onCreateActivityView(it, activityBinder, this)
        setContentView(view)
        activityBinder.performViewBinding(it, view)
    }
}

fun <T : IViewModel> Activity.activityContainer(
    dispatcher: ViewModelDispatcher<T>,
    adapter: ViewBinderAdapter? = null
): ActivityViewContainer<T> {
    return ActivityViewContainerImpl<T>(
        this, dispatcher
    ) {
        val realAdapter = adapter
            ?: it.getViewBinderAdapterFromExtension()
            ?: throw IllegalArgumentException("ViewBinderAdapter should be provided by parameters or Context.installViewBinderAdapterPlugin()")

        @Suppress("UNCHECKED_CAST")
        val activityBinder = realAdapter.getBinder(it) as ViewBinder<T>
        val viewProvider = activityBinder as? ActivityViewProvider ?: DefaultActivityViewProvider
        val view = viewProvider.onCreateActivityView(it, activityBinder, this)
        setContentView(view)
        activityBinder.performViewBinding(it, view)
    }
}

interface ActivityViewContainer<T : IViewModel> {

    enum class DestroyBehavior {
        TerminateAlways,
        TerminateIfFinishing,
        KeepAlive
    }

    fun onActivityCreate()

    fun onActivityStart()

    fun onActivityResume()

    fun onActivityPause()

    fun onActivityStop()

    fun onActivityDestroy(
        destroyBehavior: DestroyBehavior = DestroyBehavior.TerminateIfFinishing
    )

}

private class ActivityViewContainerImpl<T : IViewModel>(
    private val activity: Activity,
    private val dispatcher: ViewModelDispatcher<T>,
    private val onBindAction: (T) -> Unit
) : ActivityViewContainer<T>, ViewContainer<T> {

    private lateinit var lifecycleController: ViewModelLifecycleController
    private lateinit var viewModel: T

    override fun onAddedViewModel(viewModel: T) {
        this.viewModel = viewModel
        lifecycleController = ViewModelLifecycleController(viewModel)
    }

    override fun onViewModelRequestedCloseSelf() {
        activity.finish()
    }

    override fun onActivityCreate() {
        dispatcher.registerContainer(this)
        lifecycleController.setBound(true)
        onBindAction(viewModel)
    }

    override fun onActivityStart() {
        lifecycleController.setVisible(true)
    }

    override fun onActivityResume() {
        lifecycleController.setFocused(true)
    }

    override fun onActivityPause() {
        lifecycleController.setFocused(false)
    }

    override fun onActivityStop() {
        lifecycleController.setVisible(false)
    }

    override fun onActivityDestroy(destroyBehavior: ActivityViewContainer.DestroyBehavior) {
        lifecycleController.setBound(false)
        when (destroyBehavior) {
            ActivityViewContainer.DestroyBehavior.TerminateAlways -> {
                dispatcher.recycleViewModel()
            }
            ActivityViewContainer.DestroyBehavior.TerminateIfFinishing -> {
                if (activity.isFinishing) {
                    dispatcher.recycleViewModel()
                }
            }
            ActivityViewContainer.DestroyBehavior.KeepAlive -> {
                // do nothing
            }
        }
        dispatcher.unregisterContainer(this)
    }

}