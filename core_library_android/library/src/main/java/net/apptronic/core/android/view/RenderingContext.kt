package net.apptronic.core.android.view

import net.apptronic.core.context.Context
import net.apptronic.core.context.ContextPlugins
import net.apptronic.core.context.coroutines.CoroutineDispatchers
import net.apptronic.core.context.di.DependencyDispatcher
import net.apptronic.core.context.lifecycle.BASE_LIFECYCLE
import net.apptronic.core.context.lifecycle.Lifecycle
import net.apptronic.core.context.plugin.Extensions
import net.apptronic.core.context.plugin.Plugin
import net.apptronic.core.context.plugin.PluginDescriptor
import net.apptronic.core.view.CoreViewContext

private class RenderingContext(override val parent: CoreViewContext) : Context {

    override val lifecycle: Lifecycle = BASE_LIFECYCLE.createLifecycle()

    init {
        parent.lifecycle.registerChildLifecycle(lifecycle)
    }

    override val coroutineDispatchers: CoroutineDispatchers
        get() {
            throw UnsupportedOperationException("RenderingContext does not support CoroutineDispatchers")
        }

    override val dependencyDispatcher: DependencyDispatcher
        get() {
            throw UnsupportedOperationException("RenderingContext does not support DependencyDispatcher")
        }

    override val plugins: ContextPlugins
        get() {
            throw UnsupportedOperationException("RenderingContext does not support ContextPlugins")
        }

    override val extensions: Extensions
        get() {
            throw UnsupportedOperationException("RenderingContext does not support Extensions")
        }

    override fun <T : Plugin> installPlugin(descriptor: PluginDescriptor<T>, plugin: T) {
        throw UnsupportedOperationException("RenderingContext does not support Plugins")
    }

}

fun CoreViewContext.childRenderingContext(): Context {
    return RenderingContext(this)
}