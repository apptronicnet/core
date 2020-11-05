package net.apptronic.core.context.component

import net.apptronic.core.context.Context
import net.apptronic.core.context.di.DependencyDescriptor
import net.apptronic.core.context.di.DependencyDispatcher

/**
 * Add dependency instance to [DependencyDispatcher] of current component [Context].
 */
inline fun <reified T : Any> IComponent.dependencyInstance(instance: T) {
    context.dependencyDispatcher.addInstance(instance)
}

/**
 * Add dependency instance to [DependencyDispatcher] of current component [Context].
 */
fun <T : Any> IComponent.dependencyInstance(descriptor: DependencyDescriptor<T>, instance: T) {
    context.dependencyDispatcher.addInstance(descriptor, instance)
}