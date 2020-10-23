package net.apptronic.core.component.plugin

import net.apptronic.core.component.Component
import net.apptronic.core.component.IComponent
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.di.DependencyDispatcher

/**
 * Base class which can extend functionality for [Context] and [Component]. Plugin applies to target [Context]
 * and all children if it
 */
interface Plugin {

    /**
     * Called when plugin is installed on root plugin context
     */
    fun onInstall(context: Context) {
        // implement by subclasses if needed
    }

    /**
     * Called when root plugin context or any child context is initialized
     */
    fun onContext(context: Context) {
        // implement by subclasses if needed
    }

    /**
     * Called when any component is initialized
     */
    fun onComponent(component: IComponent) {
        // implement by subclasses if needed
    }

    /**
     * Called when [DependencyDispatcher] created new [instance] for injection.
     *
     * This is called once dependency is
     */
    fun <T> onProvide(
            definitionContext: Context,
            instance: T
    ): T {
        return instance
    }

    /**
     * Called when [DependencyDispatcher] injecting [instance] somewhere
     */
    fun <T> onInject(
            definitionContext: Context,
            injectionContext: Context,
            instance: T
    ): T {
        return instance
    }

}