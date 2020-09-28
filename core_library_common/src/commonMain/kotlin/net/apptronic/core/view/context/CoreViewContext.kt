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
import net.apptronic.core.view.CoreViewStyle
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.base.ViewConfiguration

class CoreViewContext : Context {

    private val themes = mutableListOf<CoreViewStyle>()

    fun theme(target: ICoreView, theme: CoreViewStyle) {
        theme.applyTo(target)
        themes.add(theme)
    }

    fun applyThemes(target: ICoreView) {
        parent?.applyThemes(target)
        themes.forEach {
            it.applyTo(target)
        }
    }

    override val parent: CoreViewContext?
    val viewConfiguration: ViewConfiguration
    override val lifecycle: Lifecycle

    private constructor(parent: CoreViewContext) {
        this.parent = parent
        this.lifecycle = CoreViewLifecycle.createLifecycle()
        parent.lifecycle.registerChildLifecycle(lifecycle)
        this.viewConfiguration = parent.viewConfiguration
    }

    constructor(viewConfiguration: ViewConfiguration) {
        this.parent = null
        this.lifecycle = CoreViewLifecycle.createLifecycle()
        this.viewConfiguration = viewConfiguration
    }

    private var dependencyDispatcherInstance: DependencyDispatcher? = null

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

    fun createChild(): CoreViewContext {
        return CoreViewContext(this)
    }

}