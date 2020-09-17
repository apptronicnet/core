package net.apptronic.core.android.plugins

import net.apptronic.core.android.viewmodel.transitions.TransitionBuilder
import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.plugin.Plugin
import net.apptronic.core.component.plugin.extensionDescriptor
import net.apptronic.core.component.plugin.pluginDescriptor
import net.apptronic.core.mvvm.viewmodel.ViewModel

@Deprecated("Replaced by ViewTransitionAdapter")
fun Context.installTransitionBuilderPlugin(builder: TransitionBuilder) {
    installPlugin(ViewBinderFactoryPluginDescriptor, TransitionBuilderPlugin(builder))
}

@Deprecated("Replaced by ViewTransitionAdapter")
private val ViewBinderFactoryPluginDescriptor = pluginDescriptor<TransitionBuilderPlugin>()

@Deprecated("Replaced by ViewTransitionAdapter")
private class TransitionBuilderPlugin(
    private val builder: TransitionBuilder
) : Plugin() {

    override fun onComponent(component: Component) {
        super.onComponent(component)
        if (component is ViewModel) {
            component.extensions[DefaultTransitionBuilderExtension] = builder
        }
    }

}

@Deprecated("Replaced by ViewTransitionAdapter")
private val DefaultTransitionBuilderExtension = extensionDescriptor<TransitionBuilder>()

@Deprecated("Replaced by ViewTransitionAdapter")
fun ViewModel.getTransitionBuilderFromExtension(): TransitionBuilder? {
    return extensions[DefaultTransitionBuilderExtension]
}