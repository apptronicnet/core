package net.apptronic.core.context.component

import net.apptronic.core.context.Context
import net.apptronic.core.context.plugin.extensionDescriptor

private val ComponentsExtensionDescriptor = extensionDescriptor<ContextComponentsExtension>()

private class ContextComponentsExtension {

    val components = mutableListOf<IComponent>()

}

internal fun Context.addComponent(component: IComponent) {
    val registry = context.extensions.get(ComponentsExtensionDescriptor, ::ContextComponentsExtension)
    registry.components.add(component)
    context.plugins.nextComponent(component)
}

fun Context.getComponents(): List<IComponent> {
    return extensions[ComponentsExtensionDescriptor]?.components ?: emptyList()
}