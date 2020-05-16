package net.apptronic.core.android.viewmodel

import android.app.Activity
import net.apptronic.core.android.plugins.getAndroidViewFactoryFromExtension
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.dispatcher.ViewContainer
import net.apptronic.core.mvvm.viewmodel.dispatcher.ViewModelDispatcher
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelLifecycleController

fun <T : ViewModel> Activity.activityContainer(
    dispatcher: ViewModelDispatcher<T>,
    activityView: AndroidView<T>
): ActivityViewContainer<T> {
    return ActivityViewContainerImpl<T>(
        this, dispatcher
    ) {
        val view = activityView.onCreateActivityView(this)
        setContentView(view)
        activityView.bindView(view, it)
    }
}

fun <T : ViewModel> Activity.activityContainer(
    dispatcher: ViewModelDispatcher<T>,
    factory: AndroidViewFactory? = null
): ActivityViewContainer<T> {
    return ActivityViewContainerImpl<T>(
        this, dispatcher
    ) {
        val useFactory = factory
            ?: it.getAndroidViewFactoryFromExtension()
            ?: throw IllegalArgumentException("AndroidViewFactory should be provided by parameters or Context.installViewFactoryPlugin()")
        val activityView = useFactory.getAndroidView(it) as AndroidView<T>
        val view = activityView.onCreateActivityView(this)
        setContentView(view)
        activityView.bindView(view, it)
    }
}

interface ActivityViewContainer<T : ViewModel> {

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

private class ActivityViewContainerImpl<T : ViewModel>(
    private val activity: Activity,
    private val dispatcher: ViewModelDispatcher<T>,
    private val onBindAction: (T) -> Unit
) : ActivityViewContainer<T>, ViewContainer<T> {

    private lateinit var lifecycleController: ViewModelLifecycleController

    override fun onAddedViewModel(viewModel: T) {
        viewModel.doOnBind {
            onBindAction(viewModel)
        }
        lifecycleController = ViewModelLifecycleController(viewModel)
    }

    override fun onViewModelRequestedCloseSelf() {
        activity.finish()
    }

    override fun onActivityCreate() {
        dispatcher.registerContainer(this)
        lifecycleController.setCreated(true)
        lifecycleController.setBound(true)
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