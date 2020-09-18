package net.apptronic.core.android.plugins

import net.apptronic.core.android.anim.factory.ViewTransitionFactory
import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.plugin.Plugin
import net.apptronic.core.component.plugin.extensionDescriptor
import net.apptronic.core.component.plugin.pluginDescriptor
import net.apptronic.core.mvvm.viewmodel.ViewModel

fun Context.installViewTransitionFactoryPlugin(factory: ViewTransitionFactory) {
    installPlugin(ViewTransitionFactoryPluginDescriptor, ViewTransitionFactoryPlugin(factory))
}

private val ViewTransitionFactoryPluginDescriptor = pluginDescriptor<ViewTransitionFactoryPlugin>()

private class ViewTransitionFactoryPlugin(
    private val factory: ViewTransitionFactory
) : Plugin() {

    override fun onComponent(component: Component) {
        super.onComponent(component)
        if (component is ViewModel) {
            component.extensions[DefaultViewTransitionFactoryExtensionDescriptor] = factory
        }
    }

}

private val DefaultViewTransitionFactoryExtensionDescriptor =
    extensionDescriptor<ViewTransitionFactory>()

fun ViewModel.getDefaultViewTransitionFactoryFromPlugin(): ViewTransitionFactory? {
    return extensions[DefaultViewTransitionFactoryExtensionDescriptor]
}