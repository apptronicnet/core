package net.apptronic.core.component.di

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.isolatedExecute

/**
 * Execute some [action] with instance provided by isolated [Context] with will be recycled immediately after execution
 */
fun <T, R> DependencyDispatcher.isolatedExecuteOn(descriptor: DependencyDescriptor<T>, action: (T) -> R): R {
    return context.isolatedExecute {
        val instance = dependencyDispatcher.inject(descriptor)
        action.invoke(instance)
    }
}