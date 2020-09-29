package net.apptronic.core.android.plugins

import net.apptronic.core.android.viewmodel.ViewBinderFactory
import net.apptronic.core.component.IComponent
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.plugin.Plugin
import net.apptronic.core.component.plugin.extensionDescriptor
import net.apptronic.core.component.plugin.pluginDescriptor
import net.apptronic.core.mvvm.viewmodel.IViewModel

fun Context.installBinderFactoryPlugin(factory: ViewBinderFactory) {
    installPlugin(ViewBinderFactoryPluginDescriptor, ViewBinderFactoryPlugin(factory))
}

private val ViewBinderFactoryPluginDescriptor = pluginDescriptor<ViewBinderFactoryPlugin>()

private class ViewBinderFactoryPlugin(
    private val factory: ViewBinderFactory
) : Plugin() {

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