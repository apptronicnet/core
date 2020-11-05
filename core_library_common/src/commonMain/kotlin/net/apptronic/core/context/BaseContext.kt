package net.apptronic.core.context

import net.apptronic.core.context.plugin.Extensions
import net.apptronic.core.context.plugin.Plugin
import net.apptronic.core.context.plugin.PluginDescriptor

abstract class BaseContext : Context {

    final override val extensions: Extensions = Extensions()

    final override val plugins = ContextPlugins()

    final override fun <T : Plugin> installPlugin(descriptor: PluginDescriptor<T>, plugin: T) {
        plugins.installPlugin(descriptor, plugin, this)
    }

}