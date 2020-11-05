package net.apptronic.core.android.plugins

import net.apptronic.core.android.anim.factory.ViewTransitionFactory
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.IComponent
import net.apptronic.core.context.plugin.Plugin
import net.apptronic.core.context.plugin.extensionDescriptor
import net.apptronic.core.context.plugin.pluginDescriptor
import net.apptronic.core.viewmodel.IViewModel

fun Context.installViewTransitionFactoryPlugin(factory: ViewTransitionFactory) {
    installPlugin(ViewTransitionFactoryPluginDescriptor, ViewTransitionFactoryPlugin(factory))
}

private val ViewTransitionFactoryPluginDescriptor = pluginDescriptor<ViewTransitionFactoryPlugin>()

private class ViewTransitionFactoryPlugin(
    private val factory: ViewTransitionFactory
) : Plugin {

    override fun onComponent(component: IComponent) {
        super.onComponent(component)
        if (component is IViewModel) {
            component.extensions[DefaultViewTransitionFactoryExtensionDescriptor] = factory
        }
    }

}

private val DefaultViewTransitionFactoryExtensionDescriptor =
    extensionDescriptor<ViewTransitionFactory>()

fun IViewModel.getDefaultViewTransitionFactoryFromPlugin(): ViewTransitionFactory? {
    return extensions[DefaultViewTransitionFactoryExtensionDescriptor]
}