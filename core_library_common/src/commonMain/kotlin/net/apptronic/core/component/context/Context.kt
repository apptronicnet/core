package net.apptronic.core.component.context

import kotlinx.coroutines.CoroutineDispatcher
import net.apptronic.core.component.di.DependencyDispatcher
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.plugin.ContextPlugins
import net.apptronic.core.component.plugin.Extendable
import net.apptronic.core.component.plugin.Plugin
import net.apptronic.core.component.plugin.PluginDescriptor

/**
 * Base instance for working with framework [Context] represents logical process in application.
 * All application using framework should be built as tree of [Context] instances. Each context
 * contains [Lifecycle], specifies own [Scheduler] and provides [DependencyDispatcher]
 */
interface Context : Extendable {

    val defaultDispatcher: CoroutineDispatcher

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
