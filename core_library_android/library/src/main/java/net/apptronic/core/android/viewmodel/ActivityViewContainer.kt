package net.apptronic.core.android.viewmodel

import android.app.Activity
import net.apptronic.core.android.plugins.getViewBinderFactoryFromExtension
import net.apptronic.core.android.viewmodel.view.ActivityDelegate
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.dispatcher.ViewContainer
import net.apptronic.core.mvvm.viewmodel.dispatcher.ViewModelDispatcher
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelLifecycleController

fun <T : IViewModel> Activity.activityContainer(
    dispatcher: ViewModelDispatcher<T>,
    activityBinder: ViewBinder<T>
): ActivityViewContainer<T> {
    return ActivityViewContainerImpl<T>(
        this, dispatcher
    ) {
        val delegate = activityBinder.getViewDelegate<ActivityDelegate<*>>()
        val view = delegate.performCreateActivityView(it, activityBinder, this)
        setContentView(view)
        activityBinder.performViewBinding(it, view)
    }
}

fun <T : IViewModel> Activity.activityContainer(
    dispatcher: ViewModelDispatcher<T>,
    factory: ViewBinderFactory? = null
): ActivityViewContainer<T> {
    return ActivityViewContainerImpl<T>(
        this, dispatcher
    ) {
        val useFactory = factory
            ?: it.getViewBinderFactoryFromExtension()
            ?: throw IllegalArgumentException("ViewBinderFactory should be provided by parameters or Context.installViewFactoryPlugin()")
        val activityBinder = useFactory.getBinder(it) as ViewBinder<T>
        val delegate = activityBinder.getViewDelegate<ActivityDelegate<*>>()
        val view = delegate.performCreateActivityView(it, activityBinder, this)
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
        lifecycleController.setAttached(true)
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