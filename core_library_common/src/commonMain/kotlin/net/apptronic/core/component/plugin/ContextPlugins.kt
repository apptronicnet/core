package net.apptronic.core.component.plugin

interface ContextPlugins {

    val descriptors: Array<PluginDescriptor<*>>

    val plugins: Array<Plugin>

    operator fun <T : Plugin> get(descriptor: PluginDescriptor<T>): T?

}