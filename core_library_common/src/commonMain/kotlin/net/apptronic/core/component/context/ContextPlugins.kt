package net.apptronic.core.component.context

import net.apptronic.core.component.IComponent
import net.apptronic.core.component.plugin.Plugin
import net.apptronic.core.component.plugin.PluginDescriptor

class ContextPlugins internal constructor() {

    init {
        listOf<Any>()
    }

    private val map = mutableMapOf<PluginDescriptor<*>, Plugin>()

    val descriptors: Set<PluginDescriptor<*>>
        get() = map.keys

    private val plugins: Array<Plugin>
        get() = map.values.toTypedArray()

    fun attach(context: Context) {
        val parentContext = context.parent
        if (parentContext != null) {
            map.putAll(parentContext.plugins.map)
        }
        nextContext(context)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Plugin> get(descriptor: PluginDescriptor<T>): T? {
        return map[descriptor] as? T
    }

    fun installPlugin(descriptor: PluginDescriptor<*>, plugin: Plugin, context: Context) {
        map[descriptor] = plugin
        plugin.onInstall(context)
        plugin.onContext(context)
    }

    private fun nextContext(context: Context) {
        plugins.forEach {
            it.onContext(context)
        }
    }

    fun nextComponent(component: IComponent) {
        plugins.forEach {
            it.onComponent(component)
        }
    }

    fun <E> nextProvide(definitionContext: Context, instance: E): E {
        var result: E = instance
        plugins.forEach {
            result = it.onProvide(definitionContext, result)
        }
        return result
    }

    fun <E> nextInject(definitionContext: Context, injectionContext: Context, instance: E): E {
        var result: E = instance
        plugins.forEach {
            result = it.onInject(definitionContext, injectionContext, result)
        }
        return result
    }

}