package net.apptronic.core.component

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.di.DependencyDispatcher

/**
 * Add dependency instance to [DependencyDispatcher] of current component [Context].
 */
inline fun <reified T : Any> IComponent.dependencyInstance(instance: T) {
    context.dependencyDispatcher.addInstance(instance)
}