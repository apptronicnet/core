package net.apptronic.core.context

import net.apptronic.core.context.di.DependencyDispatcher
import net.apptronic.core.context.di.DependencyProvider
import net.apptronic.core.context.di.dependencyDispatcher
import net.apptronic.core.context.lifecycle.Lifecycle
import net.apptronic.core.context.plugin.Extensions
import net.apptronic.core.context.plugin.Plugin
import net.apptronic.core.context.plugin.PluginDescriptor

abstract class BaseContext : Context {

    final override val lifecycle: Lifecycle = Lifecycle()
    final override val extensions: Extensions = Extensions()
    final override val plugins = ContextPlugins()
    final override val dependencyDispatcher: DependencyDispatcher = dependencyDispatcher()
    final override val dependencyProvider: DependencyProvider
        get() = dependencyDispatcher

    final override fun <T : Plugin> installPlugin(descriptor: PluginDescriptor<T>, plugin: T) {
        plugins.installPlugin(descriptor, plugin, this)
    }

}