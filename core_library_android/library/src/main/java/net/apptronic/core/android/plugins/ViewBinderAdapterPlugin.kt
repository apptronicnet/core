package net.apptronic.core.android.plugins

import net.apptronic.core.android.viewmodel.ViewBinderAdapter
import net.apptronic.core.context.Context
import net.apptronic.core.context.plugin.Plugin
import net.apptronic.core.context.plugin.extensionDescriptor
import net.apptronic.core.context.plugin.pluginDescriptor
import net.apptronic.core.viewmodel.IViewModel

fun Context.installViewBinderAdapterPlugin(adapter: ViewBinderAdapter) {
    installPlugin(ViewBinderAdapterPluginDescriptor, ViewBinderAdapterPlugin(adapter))
}

private val ViewBinderAdapterPluginDescriptor = pluginDescriptor<ViewBinderAdapterPlugin>()

private class ViewBinderAdapterPlugin(
    private val adapter: ViewBinderAdapter
) : Plugin {

    override fun onViewModelAttached(viewModel: IViewModel) {
        super.onViewModelAttached(viewModel)
        viewModel.extensions[DefaultViewBinderAdapterExtension] = adapter
    }

}

private val DefaultViewBinderAdapterExtension = extensionDescriptor<ViewBinderAdapter>()

fun IViewModel.getViewBinderAdapterFromExtension(): ViewBinderAdapter? {
    return extensions[DefaultViewBinderAdapterExtension]
}

fun Context.requireViewBinderAdapterPlugin() {
    if (!plugins.descriptors.contains(ViewBinderAdapterPluginDescriptor)) {
        throw IllegalStateException("ViewBinderAdapterPlugin required. Please call installViewBinderAdapterPlugin() first.")
    }
}