package net.apptronic.core.android.viewmodel

import android.app.Activity
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.dispatcher.UiContainer
import net.apptronic.core.mvvm.viewmodel.dispatcher.ViewModelDispatcher
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelLifecycleController

fun <T : ViewModel> Activity.activityUiContainer(
    dispatcher: ViewModelDispatcher<T>,
    activityView: AndroidView<T>
): ActivityUiContainer<T> {
    return ActivityUiContainerImpl<T>(
        this, dispatcher
    ) {
        val view = activityView.onCreateActivityView(this)
        setContentView(view)
        activityView.bindView(view, it)
    }
}

fun <T : ViewModel> Activity.activityUiContainer(
    dispatcher: ViewModelDispatcher<T>,
    factory: AndroidViewFactory
): ActivityUiContainer<T> {
    return ActivityUiContainerImpl<T>(
        this, dispatcher
    ) {
        val activityView = factory.getAndroidView(it) as AndroidView<T>
        val view = activityView.onCreateActivityView(this)
        setContentView(view)
        activityView.bindView(view, it)
    }
}

interface ActivityUiContainer<T : ViewModel> {

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

private class ActivityUiContainerImpl<T : ViewModel>(
    private val activity: Activity,
    private val dispatcher: ViewModelDispatcher<T>,
    private val onBindAction: (T) -> Unit
) : ActivityUiContainer<T>, UiContainer<T> {

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

    override fun onActivityDestroy(destroyBehavior: ActivityUiContainer.DestroyBehavior) {
        lifecycleController.setBound(false)
        when (destroyBehavior) {
            ActivityUiContainer.DestroyBehavior.TerminateAlways -> {
                dispatcher.recycleViewModel()
            }
            ActivityUiContainer.DestroyBehavior.TerminateIfFinishing -> {
                if (activity.isFinishing) {
                    dispatcher.recycleViewModel()
                }
            }
            ActivityUiContainer.DestroyBehavior.KeepAlive -> {
                // do nothing
            }
        }
        dispatcher.unregisterContainer(this)
    }

}