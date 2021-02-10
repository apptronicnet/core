package net.apptronic.core.android.plugins

import android.app.Activity
import android.app.Application
import net.apptronic.core.android.anim.adapter.ViewTransitionAdapter
import net.apptronic.core.android.viewmodel.ViewBinderAdapter
import net.apptronic.core.context.Context
import net.apptronic.core.context.plugin.Plugin
import net.apptronic.core.context.plugin.pluginDescriptor
import net.apptronic.core.viewmodel.IViewModel
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
) : Plugin {

    private var viewBinderAdapter: ViewBinderAdapter? = null
    private var viewTransitionAdapter: ViewTransitionAdapter? = null
    private val activityBindingPlugin = ActivityBindingPlugin(androidApplication)

    class Builder internal constructor(androidApplication: Application) {

        internal val target = AndroidApplicationPlugin(androidApplication)

        fun installViewBinderAdapter(viewBinderAdapter: ViewBinderAdapter) {
            target.viewBinderAdapter = viewBinderAdapter
        }

        fun installViewTransitionAdapter(viewTransitionAdapter: ViewTransitionAdapter) {
            target.viewTransitionAdapter = viewTransitionAdapter
        }

        fun <A : Activity, VM : IViewModel> bindActivity(
            activity: KClass<A>, viewModel: KClass<VM>, onBackPressed: ((VM) -> Boolean)? = null
        ) {
            target.activityBindingPlugin.bindActivity(activity, viewModel, onBackPressed)
        }
    }

    override fun onInstall(context: Context) {
        super.onInstall(context)
        viewBinderAdapter?.let {
            context.installViewBinderAdapterPlugin(it)
        }
        viewTransitionAdapter?.let {
            context.installViewTransitionAdapterPlugin(it)
        }
        context.installPlugin(ActivityBindingPluginDescriptor, activityBindingPlugin)
    }

}