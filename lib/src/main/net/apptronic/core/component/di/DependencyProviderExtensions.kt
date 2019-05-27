package net.apptronic.core.component.di

import net.apptronic.core.component.context.Context

/**
 * Execute some [action] with instance provided by isolated [Context] with will be recycled immediately after execution
 */
fun <T, R> DependencyProvider.isolatedExecuteOn(descriptor: Descriptor<T>, action: (T) -> R): R {
    return context.isolatedExecute {
        val instance = getProvider().inject(descriptor)
        action.invoke(instance)
    }
}