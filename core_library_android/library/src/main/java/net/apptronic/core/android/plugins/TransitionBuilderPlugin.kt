package net.apptronic.core.android.plugins

import net.apptronic.core.android.viewmodel.transitions.TransitionBuilder
import net.apptronic.core.component.IComponent
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.plugin.Plugin
import net.apptronic.core.component.plugin.extensionDescriptor
import net.apptronic.core.component.plugin.pluginDescriptor
import net.apptronic.core.mvvm.viewmodel.IViewModel

@Deprecated("Replaced by ViewTransitionFactory")
fun Context.installTransitionBuilderPlugin(builder: TransitionBuilder) {
    installPlugin(ViewBinderFactoryPluginDescriptor, TransitionBuilderPlugin(builder))
}

@Deprecated("Replaced by ViewTransitionFactory")
private val ViewBinderFactoryPluginDescriptor = pluginDescriptor<TransitionBuilderPlugin>()

@Deprecated("Replaced by ViewTransitionFactory")
private class TransitionBuilderPlugin(
    private val builder: TransitionBuilder
) : Plugin() {

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