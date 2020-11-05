package net.apptronic.core.view

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.context.Context
import net.apptronic.core.context.ContextPlugins
import net.apptronic.core.context.coroutines.CoroutineDispatchers
import net.apptronic.core.context.di.DependencyDispatcher
import net.apptronic.core.context.lifecycle.BASE_LIFECYCLE
import net.apptronic.core.context.lifecycle.Lifecycle
import net.apptronic.core.context.plugin.Extensions
import net.apptronic.core.context.plugin.Plugin
import net.apptronic.core.context.plugin.PluginDescriptor
import net.apptronic.core.context.terminate

@UnderDevelopment
class CoreViewContext : Context {

    private var parentReference: Context? = null

    override val parent: Context?
        get() = parentReference
    override val lifecycle: Lifecycle = BASE_LIFECYCLE.createLifecycle()

    internal fun setParentContext(parent: Context) {
        if (parent.lifecycle.isTerminated()) {
            terminate()
        } else {
            parent.lifecycle.registerChildLifecycle(lifecycle)
        }
    }

    override val coroutineDispatchers: CoroutineDispatchers
        get() {
            throw UnsupportedOperationException()
        }

    override val plugins: ContextPlugins
        get() {
            throw UnsupportedOperationException("Plugins are not supported in CoreViewContext")
        }

    override val dependencyDispatcher: DependencyDispatcher
        get() {
            throw UnsupportedOperationException("Plugins are not supported in CoreViewContext")
        }

    override val extensions: Extensions
        get() {
            throw UnsupportedOperationException("Extensions are not supported in CoreViewContext")
        }

    override fun <T : Plugin> installPlugin(descriptor: PluginDescriptor<T>, plugin: T) {
        throw UnsupportedOperationException("Plugins are not supported in CoreViewContext")
    }

}