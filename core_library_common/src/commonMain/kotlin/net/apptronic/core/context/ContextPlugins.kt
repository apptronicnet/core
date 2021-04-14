package net.apptronic.core.context

import net.apptronic.core.context.plugin.Plugin
import net.apptronic.core.context.plugin.PluginDescriptor
import net.apptronic.core.viewmodel.IViewModel

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
        plugins.forEach {
            it.onContext(context)
        }
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

    fun nextViewModelAttached(viewModel: IViewModel) {
        plugins.forEach {
            it.onViewModelAttached(viewModel)
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