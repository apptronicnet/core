package net.apptronic.core.android.plugins

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcherOwner
import net.apptronic.core.android.viewmodel.activityContainer
import net.apptronic.core.component.IComponent
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.plugin.Plugin
import net.apptronic.core.component.plugin.pluginDescriptor
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.dispatcher.ViewModelDispatcher
import kotlin.reflect.KClass

val ActivityBindingPluginDescriptor = pluginDescriptor<ActivityBindingPlugin>()

class ActivityBindingPlugin(
        private val androidApplication: Application
) : Plugin(), Application.ActivityLifecycleCallbacks {

    private class ActivityBinding<A : Activity, VM : IViewModel>(
        val activity: KClass<A>,
        val viewModel: KClass<VM>,
        val onActivityBackPressed: ((VM) -> Boolean)?
    ) {

        @Suppress("UNCHECKED_CAST")
        fun buildBackPressedCallback(
            activity: Activity,
            viewModel: IViewModel
        ): OnBackPressedCallback? {
            if (onActivityBackPressed != null) {
                val vm = viewModel as? VM
                if (vm != null) {
                    return ViewModelBackPressedCallback(activity, vm, onActivityBackPressed)
                }
            }
            return null
        }

    }

    private class ActivityInstance(
            val activity: Activity,
            val dispatcher: ViewModelDispatcher<*>
    ) {
        val viewContainer = activity.activityContainer(dispatcher)
    }

    private class ViewModelBackPressedCallback<T : IViewModel>(
        private val activity: Activity,
        private val viewModel: T,
        private val handler: (T) -> Boolean
    ) : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val result = handler.invoke(viewModel)
            if (!result) {
                activity.finish()
            }
        }
    }

    private val bindings = mutableListOf<ActivityBinding<*, *>>()
    private val dispatchers = mutableListOf<ViewModelDispatcher<*>>()

    fun <A : Activity, VM : IViewModel> bindActivity(
        activity: KClass<A>, viewModel: KClass<VM>, onBackPressed: ((VM) -> Boolean)? = null
    ) {
        bindings.add(ActivityBinding(activity, viewModel, onBackPressed))
    }

    private val instances = mutableListOf<ActivityInstance>()

    init {
        androidApplication.registerActivityLifecycleCallbacks(this)
    }

    override fun onInstall(context: Context) {
        super.onInstall(context)
        context.requireViewBinderFactoryPlugin()
    }

    override fun onComponent(component: IComponent) {
        super.onComponent(component)
        if (component is ViewModelDispatcher<*>) {
            dispatchers.add(component)
        }
    }

    private fun Activity.instance(): ActivityInstance? {
        return instances.firstOrNull {
            it.activity == this
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val binding = bindings.firstOrNull {
            it.activity.java == activity.javaClass
        }
        if (binding != null) {
            val dispatcher = dispatchers.firstOrNull {
                it.viewModelType() == binding.viewModel
            }
            if (dispatcher != null) {
                instances.add(ActivityInstance(activity, dispatcher))
                if (activity is OnBackPressedDispatcherOwner) {
                    val backPressedCallback =
                            binding.buildBackPressedCallback(activity, dispatcher.getViewModel())
                    if (backPressedCallback != null) {
                        activity.onBackPressedDispatcher.addCallback(backPressedCallback)
                    }
                }
            }
        }
        activity.instance()?.viewContainer?.onActivityCreate()
    }

    override fun onActivityStarted(activity: Activity) {
        activity.instance()?.viewContainer?.onActivityStart()
    }

    override fun onActivityResumed(activity: Activity) {
        activity.instance()?.viewContainer?.onActivityResume()
    }

    override fun onActivityPaused(activity: Activity) {
        activity.instance()?.viewContainer?.onActivityPause()
    }

    override fun onActivityStopped(activity: Activity) {
        activity.instance()?.viewContainer?.onActivityStop()
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        // ignore
    }

    override fun onActivityDestroyed(activity: Activity) {
        activity.instance()?.viewContainer?.onActivityDestroy()
        activity.instance()?.let {
            instances.remove(it)
        }
    }

}