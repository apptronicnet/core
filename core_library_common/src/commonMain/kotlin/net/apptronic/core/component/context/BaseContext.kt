package net.apptronic.core.component.context

import net.apptronic.core.component.plugin.Extensions
import net.apptronic.core.component.plugin.Plugin
import net.apptronic.core.component.plugin.PluginDescriptor

abstract class BaseContext : Context {

    final override val extensions: Extensions = Extensions()

    final override val plugins = ContextPlugins()

    final override fun <T : Plugin> installPlugin(descriptor: PluginDescriptor<T>, plugin: T) {
        plugins.installPlugin(descriptor, plugin, this)
    }

}