package net.apptronic.core.component.context

import net.apptronic.core.component.plugin.ContextPlugins
import net.apptronic.core.component.plugin.Extensions
import net.apptronic.core.component.plugin.Plugin
import net.apptronic.core.component.plugin.PluginDescriptor

abstract class BaseContext : Context {

    final override val extensions: Extensions = Extensions()

    internal val pluginsImpl = ContextPluginsImpl()

    final override fun <T : Plugin> installPlugin(descriptor: PluginDescriptor<T>, plugin: T) {
        plugin.onInstall(this)
        pluginsImpl.add(descriptor, plugin)
        plugin.onContext(this)
    }

    final override val plugins: ContextPlugins = pluginsImpl

}