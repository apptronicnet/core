package net.apptronic.core.android.plugins

import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.IComponent
import net.apptronic.core.context.plugin.Plugin
import net.apptronic.core.context.plugin.extensionDescriptor
import net.apptronic.core.context.plugin.pluginDescriptor
import net.apptronic.core.viewmodel.IViewModel

fun Context.installBinderFactoryPlugin(factory: ViewBinderFactory) {
    installPlugin(ViewBinderFactoryPluginDescriptor, ViewBinderFactoryPlugin(factory))
}

private val ViewBinderFactoryPluginDescriptor = pluginDescriptor<ViewBinderFactoryPlugin>()

private class ViewBinderFactoryPlugin(
    private val factory: ViewBinderFactory
) : Plugin {

    override fun onComponent(component: IComponent) {
        super.onComponent(component)
        if (component is IViewModel) {
            component.extensions[DefaultViewBinderFactoryExtension] = factory
        }
    }

}

private val DefaultViewBinderFactoryExtension = extensionDescriptor<ViewBinderFactory>()

fun IViewModel.getViewBinderFactoryFromExtension(): ViewBinderFactory? {
    return extensions[DefaultViewBinderFactoryExtension]
}

fun Context.requireViewBinderFactoryPlugin() {
    if (!plugins.descriptors.contains(ViewBinderFactoryPluginDescriptor)) {
        throw IllegalStateException("ViewFactoryPlugin required. Please call installViewFactoryPlugin() first.")
    }
}