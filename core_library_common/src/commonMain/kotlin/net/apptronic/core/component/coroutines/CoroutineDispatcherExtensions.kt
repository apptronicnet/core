package net.apptronic.core.component.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.Contextual
import kotlin.coroutines.CoroutineContext

fun Contextual.coroutineDispatcher(descriptor: CoroutineDispatcherDescriptor): CoroutineDispatcher {
    return context.coroutineDispatchers[descriptor]
}

/**
 * [CoroutineDispatcher] which matches to [Dispatchers.Main] (if not overridden by [Context])
 */
val Contextual.mainDispatcher: CoroutineContext
    get() {
        return coroutineDispatcher(MainDispatcherDescriptor)
    }

/**
 * [CoroutineDispatcher] which matches to [Dispatchers.Default] (if not overridden by [Context])
 */
val Contextual.defaultDispatcher: CoroutineContext
    get() {
        return coroutineDispatcher(DefatultDispatcherDescriptor)
    }

/**
 * [CoroutineDispatcher] which matches to [Dispatchers.Unconfined] (if not overridden by [Context])
 */
val Contextual.unconfinedDispatcher: CoroutineContext
    get() {
        return coroutineDispatcher(UnconfinedDispatcherDescriptor)
    }

/**
 * Get instance of [ManualDispatcher].
 */
val Contextual.manualDispatcher: ManualDispatcher
    get() {
        return coroutineDispatcher(ManualDispatcherDescriptor) as? ManualDispatcher ?: ManualDispatcher()
    }

/**
 * Get [CoroutineContext] which uses to [Dispatchers.Default] and executes all coroutines with specified
 * [priority] (if not overridden by [Context])
 */
fun Contextual.priorityDispatcher(priority: Int = PRIORITY_MEDIUM): CoroutineContext {
    return context.coroutineDispatchers[BackgroundPriorityDispatcherDescriptor].withPriority(priority)
}