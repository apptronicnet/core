package net.apptronic.core.component.context

import net.apptronic.core.component.plugin.ContextPlugins
import net.apptronic.core.component.plugin.Plugin
import net.apptronic.core.component.plugin.PluginDescriptor

internal class ContextPluginsImpl : ContextPlugins {

    private val map = mutableMapOf<PluginDescriptor<*>, Plugin>()

    override val descriptors: Array<PluginDescriptor<*>>
        get() = map.keys.toTypedArray()

    override val plugins: Array<Plugin>
        get() = map.values.toTypedArray()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Plugin> get(descriptor: PluginDescriptor<T>): T? {
        return map[descriptor] as? T
    }

    fun add(descriptor: PluginDescriptor<*>, plugin: Plugin) {
        map[descriptor] = plugin
    }

}