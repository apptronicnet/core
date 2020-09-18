package net.apptronic.core.android.plugins

import net.apptronic.core.android.anim.factory.ViewTransitionFactory
import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.plugin.Plugin
import net.apptronic.core.component.plugin.extensionDescriptor
import net.apptronic.core.component.plugin.pluginDescriptor
import net.apptronic.core.mvvm.viewmodel.ViewModel

fun Context.installViewTransitionAdapterPlugin(factory: ViewTransitionFactory) {
    installPlugin(ViewTransitionAdapterPluginDescriptor, ViewTransitionAdapterPlugin(factory))
}

private val ViewTransitionAdapterPluginDescriptor = pluginDescriptor<ViewTransitionAdapterPlugin>()

private class ViewTransitionAdapterPlugin(
    private val factory: ViewTransitionFactory
) : Plugin() {

    override fun onComponent(component: Component) {
        super.onComponent(component)
        if (component is ViewModel) {
            component.extensions[DefaultViewTransitionAdapterExtensionDescriptor] = factory
        }
    }

}

private val DefaultViewTransitionAdapterExtensionDescriptor =
    extensionDescriptor<ViewTransitionFactory>()

fun ViewModel.getDefaultViewTransitionAdapterFromPlugin(): ViewTransitionFactory? {
    return extensions[DefaultViewTransitionAdapterExtensionDescriptor]
}