package net.apptronic.core.android.plugins

import net.apptronic.core.android.viewmodel.AndroidViewFactory
import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.plugin.ContextPlugins
import net.apptronic.core.component.plugin.Plugin
import net.apptronic.core.component.plugin.extensionDescriptor
import net.apptronic.core.component.plugin.pluginDescriptor
import net.apptronic.core.mvvm.viewmodel.ViewModel
import java.lang.IllegalStateException

fun Context.installViewFactoryPlugin(factory: AndroidViewFactory) {
    installPlugin(ViewFactoryPluginDescriptor, ViewFactoryPlugin(factory))
}

private val ViewFactoryPluginDescriptor = pluginDescriptor<ViewFactoryPlugin>()

private class ViewFactoryPlugin(
    private val factory: AndroidViewFactory
) : Plugin() {

    override fun onComponent(component: Component) {
        super.onComponent(component)
        if (component is ViewModel) {
            component.extensions[DefaultAndroidViewFactoryExtension] = factory
        }
    }

}

private val DefaultAndroidViewFactoryExtension = extensionDescriptor<AndroidViewFactory>()

fun ViewModel.getAndroidViewFactoryFromExtension(): AndroidViewFactory? {
    return extensions[DefaultAndroidViewFactoryExtension]
}

fun Context.requireViewFactoryPlugin() {
    if (!plugins.descriptors.contains(ViewFactoryPluginDescriptor)) {
        throw IllegalStateException("ViewFactoryPlugin required. Please call installViewFactoryPlugin() first.")
    }
}