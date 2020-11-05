package net.apptronic.core.context.di

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.context.Context
import net.apptronic.core.context.isolatedExecute

/**
 * Execute some [action] with instance provided by isolated [Context] with will be recycled immediately after execution
 */
@UnderDevelopment
fun <T, R> DependencyDispatcher.isolatedExecuteOn(descriptor: DependencyDescriptor<T>, action: (T) -> R): R {
    return context.isolatedExecute {
        val instance = dependencyDispatcher.inject(descriptor)
        action.invoke(instance)
    }
}