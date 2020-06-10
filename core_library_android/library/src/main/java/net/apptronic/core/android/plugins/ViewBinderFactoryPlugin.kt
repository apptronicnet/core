package net.apptronic.core.android.plugins

import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.plugin.Plugin
import net.apptronic.core.component.plugin.extensionDescriptor
import net.apptronic.core.component.plugin.pluginDescriptor
import net.apptronic.core.mvvm.viewmodel.ViewModel

fun Context.installViewFactoryPlugin(factory: ViewBinderFactory) {
    installPlugin(ViewBinderFactoryPluginDescriptor, ViewBinderFactoryPlugin(factory))
}

private val ViewBinderFactoryPluginDescriptor = pluginDescriptor<ViewBinderFactoryPlugin>()

private class ViewBinderFactoryPlugin(
    private val factory: ViewBinderFactory
) : Plugin() {

    override fun onComponent(component: Component) {
        super.onComponent(component)
        if (component is ViewModel) {
            component.extensions[DefaultViewBinderFactoryExtension] = factory
        }
    }

}

private val DefaultViewBinderFactoryExtension = extensionDescriptor<ViewBinderFactory>()

fun ViewModel.getViewBinderFactoryFromExtension(): ViewBinderFactory? {
    return extensions[DefaultViewBinderFactoryExtension]
}

fun Context.requireViewBinderFactoryPlugin() {
    if (!plugins.descriptors.contains(ViewBinderFactoryPluginDescriptor)) {
        throw IllegalStateException("ViewFactoryPlugin required. Please call installViewFactoryPlugin() first.")
    }
}