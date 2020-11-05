package net.apptronic.core.test

import kotlinx.coroutines.Dispatchers
import net.apptronic.core.context.coroutines.*

/**
 * Create a [CoroutineDispatchers] using only [Dispatchers.Unconfined] and [ManualDispatcher] for testing
 */
fun testCoroutineDispatchers(): CoroutineDispatchers {
    return CoroutineDispatchers(Dispatchers.Unconfined).also {
        it[BackgroundPriorityDispatcherDescriptor] = BackgroundPriorityDispatcher(Dispatchers.Unconfined, Dispatchers.Default)
        it[ManualDispatcherDescriptor] = ManualDispatcher()
    }
}