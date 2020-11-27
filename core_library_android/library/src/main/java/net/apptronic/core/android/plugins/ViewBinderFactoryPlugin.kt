package net.apptronic.core.android.plugins

import net.apptronic.core.android.viewmodel.ViewBinderAdapter
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.IComponent
import net.apptronic.core.context.plugin.Plugin
import net.apptronic.core.context.plugin.extensionDescriptor
import net.apptronic.core.context.plugin.pluginDescriptor
import net.apptronic.core.viewmodel.IViewModel

fun Context.installBinderFactoryPlugin(adapter: ViewBinderAdapter) {
    installPlugin(ViewBinderFactoryPluginDescriptor, ViewBinderFactoryPlugin(adapter))
}

private val ViewBinderFactoryPluginDescriptor = pluginDescriptor<ViewBinderFactoryPlugin>()

private class ViewBinderFactoryPlugin(
    private val adapter: ViewBinderAdapter
) : Plugin {

    override fun onComponent(component: IComponent) {
        super.onComponent(component)
        if (component is IViewModel) {
            component.extensions[DefaultViewBinderFactoryExtension] = adapter
        }
    }

}

private val DefaultViewBinderFactoryExtension = extensionDescriptor<ViewBinderAdapter>()

fun IViewModel.getViewBinderFactoryFromExtension(): ViewBinderAdapter? {
    return extensions[DefaultViewBinderFactoryExtension]
}

fun Context.requireViewBinderFactoryPlugin() {
    if (!plugins.descriptors.contains(ViewBinderFactoryPluginDescriptor)) {
        throw IllegalStateException("ViewFactoryPlugin required. Please call installViewFactoryPlugin() first.")
    }
}