package net.apptronic.core.android.plugins

import android.app.Activity
import android.app.Application
import net.apptronic.core.android.anim.adapter.ViewTransitionAdapter
import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.android.viewmodel.requireBoundView
import net.apptronic.core.android.viewmodel.transitions.TransitionBuilder
import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.plugin.Plugin
import net.apptronic.core.component.plugin.pluginDescriptor
import net.apptronic.core.mvvm.viewmodel.ViewModel
import kotlin.reflect.KClass

val AndroidApplicationPluginDescriptor = pluginDescriptor<AndroidApplicationPlugin>()

fun Context.installAndroidApplicationPlugin(
    androidApplication: Application,
    builder: AndroidApplicationPlugin.Builder.() -> Unit
) {
    val plugin = AndroidApplicationPlugin.Builder(androidApplication).apply(builder).target
    installPlugin(AndroidApplicationPluginDescriptor, plugin)
}

class AndroidApplicationPlugin internal constructor(
    private val androidApplication: Application
) : Plugin() {

    private var viewBinderFactory: ViewBinderFactory? = null
    private var transitionBuilder: TransitionBuilder? = null
    private var viewTransitionAdapter: ViewTransitionAdapter? = null
    private val activityBindingPlugin = ActivityBindingPlugin(androidApplication)

    class Builder internal constructor(androidApplication: Application) {

        internal val target = AndroidApplicationPlugin(androidApplication)

        fun binderFactory(viewBinderFactory: ViewBinderFactory) {
            target.viewBinderFactory = viewBinderFactory
        }

        @Deprecated("Replaced by net.apptronic.core.android.anim.*")
        fun transitionBuilder(transitionBuilder: TransitionBuilder) {
            target.transitionBuilder = transitionBuilder
        }

        fun viewTransitionAdapter(viewTransitionAdapter: ViewTransitionAdapter) {
            target.viewTransitionAdapter = viewTransitionAdapter
        }

        fun <A : Activity, VM : ViewModel> bindActivity(
            activity: KClass<A>, viewModel: KClass<VM>, onBackPressed: ((VM) -> Boolean)? = null
        ) {
            target.activityBindingPlugin.bindActivity(activity, viewModel, onBackPressed)
        }
    }

    override fun onInstall(context: Context) {
        super.onInstall(context)
        viewBinderFactory?.let {
            context.installBinderFactoryPlugin(it)
        }
        transitionBuilder?.let {
            context.installTransitionBuilderPlugin(it)
        }
        viewTransitionAdapter?.let {
            context.installViewTransitionAdapterPlugin(it)
        }
        context.installPlugin(ActivityBindingPluginDescriptor, activityBindingPlugin)
    }

    override fun onComponent(component: Component) {
        super.onComponent(component)
        if (component is ViewModel) {
            component.requireBoundView()
        }
    }

}