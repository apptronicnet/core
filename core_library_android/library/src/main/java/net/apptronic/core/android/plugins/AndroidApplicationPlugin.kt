package net.apptronic.core.android.plugins

import android.app.Activity
import android.app.Application
import net.apptronic.core.android.viewmodel.AndroidViewFactory
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

    private var viewFactory: AndroidViewFactory? = null
    private val activityBindingPlugin = ActivityBindingPlugin(androidApplication)

    class Builder internal constructor(androidApplication: Application) {

        internal val target = AndroidApplicationPlugin(androidApplication)

        fun viewFactory(viewFactory: AndroidViewFactory) {
            target.viewFactory = viewFactory
        }

        fun <A : Activity, VM : ViewModel> bindActivity(
                activity: KClass<A>, viewModel: KClass<VM>, onBackPressed: ((VM) -> Boolean)? = null
        ) {
            target.activityBindingPlugin.bindActivity(activity, viewModel, onBackPressed)
        }
    }

    override fun onInstall(context: Context) {
        super.onInstall(context)
        viewFactory?.let {
            context.installViewFactoryPlugin(it)
        }
        context.installPlugin(ActivityBindingPluginDescriptor, activityBindingPlugin)
    }

}