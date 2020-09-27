package net.apptronic.core.view.context

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.CoroutineDispatchers
import net.apptronic.core.component.di.DependencyDispatcher
import net.apptronic.core.component.di.dependencyDispatcher
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.plugin.ContextPlugins
import net.apptronic.core.component.plugin.Extensions
import net.apptronic.core.component.plugin.Plugin
import net.apptronic.core.component.plugin.PluginDescriptor

class CoreViewContext(override val parent: Context?) : Context {

    private var dependencyDispatcherInstance: DependencyDispatcher? = null

    override val coroutineDispatchers: CoroutineDispatchers
        get() {
            throw UnsupportedOperationException()
        }

    override val lifecycle: Lifecycle = CoreViewLifecycle.createLifecycle()

    override val plugins: ContextPlugins
        get() {
            throw UnsupportedOperationException("Plugins are not supported in CoreViewContext")
        }

    override val dependencyDispatcher: DependencyDispatcher
        get() {
            return dependencyDispatcherInstance ?: run {
                dependencyDispatcher().also {
                    dependencyDispatcherInstance = it
                }
            }
        }

    override val extensions: Extensions
        get() {
            throw UnsupportedOperationException("Extensions are not supported in CoreViewContext")
        }

    override fun <T : Plugin> installPlugin(descriptor: PluginDescriptor<T>, plugin: T) {
        throw UnsupportedOperationException("Plugins are not supported in CoreViewContext")
    }

}