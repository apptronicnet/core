package net.apptronic.core.component.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

val MainDispatcherDescriptor = coroutineDispatcherDescriptor("Main")
val DefatultDispatcherDescriptor = coroutineDispatcherDescriptor("Default")
val UnconfinedDispatcherDescriptor = coroutineDispatcherDescriptor("Unconfined")
val BackgroundPriorityDispatcherDescriptor = coroutineDispatcherDescriptor("BackgroundPriorityDispatcher")
val ManualDispatcherDescriptor = coroutineDispatcherDescriptor("ManualDispatcher")

/**
 * Create a [CoroutineDispatchers] using set of standard [CoroutineDispatcher].
 */
fun standardCoroutineDispatchers(): CoroutineDispatchers {
    return CoroutineDispatchers(Dispatchers.Main).also {
        it[MainDispatcherDescriptor] = Dispatchers.Main
        it[DefatultDispatcherDescriptor] = Dispatchers.Default
        it[UnconfinedDispatcherDescriptor] = Dispatchers.Unconfined
        it[BackgroundPriorityDispatcherDescriptor] = BackgroundPriorityDispatcher(Dispatchers.Main, Dispatchers.Default)
        it[ManualDispatcherDescriptor] = ManualDispatcher()
    }
}

/**
 * Create a [CoroutineDispatchers] using only [Dispatchers.Unconfined] and [ManualDispatcher].
 */
fun testCoroutineDispatchers(): CoroutineDispatchers {
    return CoroutineDispatchers(Dispatchers.Unconfined).also {
        it[BackgroundPriorityDispatcherDescriptor] = BackgroundPriorityDispatcher(Dispatchers.Unconfined, Dispatchers.Default)
        it[ManualDispatcherDescriptor] = ManualDispatcher()
    }
}

class CoroutineDispatchers(
        private val fallbackDispatcher: CoroutineDispatcher
) {

    private val dispatchers = mutableMapOf<CoroutineDispatcherDescriptor, CoroutineDispatcher>()

    operator fun set(descriptor: CoroutineDispatcherDescriptor, dispatcher: CoroutineDispatcher) {
        dispatchers[descriptor] = dispatcher
    }

    operator fun get(descriptor: CoroutineDispatcherDescriptor): CoroutineDispatcher {
        return dispatchers[descriptor] ?: fallbackDispatcher
    }

}