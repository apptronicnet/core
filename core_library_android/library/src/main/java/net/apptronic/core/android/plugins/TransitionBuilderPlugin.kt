package net.apptronic.core.android.plugins

import net.apptronic.core.android.viewmodel.transitions.TransitionBuilder
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.IComponent
import net.apptronic.core.context.plugin.Plugin
import net.apptronic.core.context.plugin.extensionDescriptor
import net.apptronic.core.context.plugin.pluginDescriptor
import net.apptronic.core.viewmodel.IViewModel

@Deprecated("Replaced by ViewTransitionFactory")
fun Context.installTransitionBuilderPlugin(builder: TransitionBuilder) {
    installPlugin(ViewBinderFactoryPluginDescriptor, TransitionBuilderPlugin(builder))
}

@Deprecated("Replaced by ViewTransitionFactory")
private val ViewBinderFactoryPluginDescriptor = pluginDescriptor<TransitionBuilderPlugin>()

@Deprecated("Replaced by ViewTransitionFactory")
private class TransitionBuilderPlugin(
    private val builder: TransitionBuilder
) : Plugin {

    override fun onComponent(component: IComponent) {
        super.onComponent(component)
        if (component is IViewModel) {
            component.extensions[DefaultTransitionBuilderExtension] = builder
        }
    }

}

@Deprecated("Replaced by ViewTransitionFactory")
private val DefaultTransitionBuilderExtension = extensionDescriptor<TransitionBuilder>()

@Deprecated("Replaced by ViewTransitionFactory")
fun IViewModel.getTransitionBuilderFromExtension(): TransitionBuilder? {
    return extensions[DefaultTransitionBuilderExtension]
}