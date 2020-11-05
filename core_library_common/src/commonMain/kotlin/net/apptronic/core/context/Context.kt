package net.apptronic.core.context

import net.apptronic.core.context.coroutines.CoroutineDispatchers
import net.apptronic.core.context.di.DependencyDispatcher
import net.apptronic.core.context.lifecycle.Lifecycle
import net.apptronic.core.context.plugin.Extendable
import net.apptronic.core.context.plugin.Plugin
import net.apptronic.core.context.plugin.PluginDescriptor

/**
 * Base instance for working with framework [Context] represents logical process in application.
 * All application using framework should be built as tree of [Context] instances. Each context
 * contains [Lifecycle], specifies own [Scheduler] and provides [DependencyDispatcher]
 */
interface Context : Extendable, Contextual {

    override val context: Context
        get() = this

    val coroutineDispatchers: CoroutineDispatchers

    val parent: Context?

    /**
     * [Lifecycle] of current [Context]
     */
    val lifecycle: Lifecycle

    val plugins: ContextPlugins

    fun <T : Plugin> installPlugin(descriptor: PluginDescriptor<T>, plugin: T)

    /**
     * Get [DependencyDispatcher] for current [Context]
     */
    val dependencyDispatcher: DependencyDispatcher

}