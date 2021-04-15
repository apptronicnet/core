package net.apptronic.core.android.plugins

import net.apptronic.core.android.anim.adapter.ViewTransitionAdapter
import net.apptronic.core.context.Context
import net.apptronic.core.context.plugin.Plugin
import net.apptronic.core.context.plugin.extensionDescriptor
import net.apptronic.core.context.plugin.pluginDescriptor
import net.apptronic.core.viewmodel.IViewModel

fun Context.installViewTransitionAdapterPlugin(adapter: ViewTransitionAdapter) {
    installPlugin(ViewTransitionAdapterPluginDescriptor, ViewTransitionAdapterPlugin(adapter))
}

private val ViewTransitionAdapterPluginDescriptor = pluginDescriptor<ViewTransitionAdapterPlugin>()

private class ViewTransitionAdapterPlugin(
    private val adapter: ViewTransitionAdapter
) : Plugin {

    override fun onViewModelAttached(viewModel: IViewModel) {
        super.onViewModelAttached(viewModel)
        viewModel.extensions[DefaultViewTransitionAdapterExtensionDescriptor] = adapter
    }

}

private val DefaultViewTransitionAdapterExtensionDescriptor =
    extensionDescriptor<ViewTransitionAdapter>()

fun IViewModel.getDefaultViewTransitionAdapterFromPlugin(): ViewTransitionAdapter? {
    return extensions[DefaultViewTransitionAdapterExtensionDescriptor]
}