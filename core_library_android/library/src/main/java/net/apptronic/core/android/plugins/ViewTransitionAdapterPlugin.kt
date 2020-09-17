package net.apptronic.core.android.plugins

import net.apptronic.core.android.anim.adapter.ViewTransitionAdapter
import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.plugin.Plugin
import net.apptronic.core.component.plugin.extensionDescriptor
import net.apptronic.core.component.plugin.pluginDescriptor
import net.apptronic.core.mvvm.viewmodel.ViewModel

fun Context.installViewTransitionAdapterPlugin(adapter: ViewTransitionAdapter) {
    installPlugin(ViewTransitionAdapterPluginDescriptor, ViewTransitionAdapterPlugin(adapter))
}

private val ViewTransitionAdapterPluginDescriptor = pluginDescriptor<ViewTransitionAdapterPlugin>()

private class ViewTransitionAdapterPlugin(
    private val adapter: ViewTransitionAdapter
) : Plugin() {

    override fun onComponent(component: Component) {
        super.onComponent(component)
        if (component is ViewModel) {
            component.extensions[DefaultViewTransitionAdapterExtensionDescriptor] = adapter
        }
    }

}

private val DefaultViewTransitionAdapterExtensionDescriptor =
    extensionDescriptor<ViewTransitionAdapter>()

fun ViewModel.getDefaultViewTransitionAdapterFromPlugin(): ViewTransitionAdapter? {
    return extensions[DefaultViewTransitionAdapterExtensionDescriptor]
}