package net.apptronic.core.component.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

val MainDispatcherDescriptor = coroutineDispatcherDescriptor("Main")
val BackgroundDispatcherDescriptor = coroutineDispatcherDescriptor("Background")
val UnconfinedDispatcherDescriptor = coroutineDispatcherDescriptor("Unconfined")

fun standardCoroutineDispatchers(): CoroutineDispatchers {
    return CoroutineDispatchers(Dispatchers.Main).also {
        it[MainDispatcherDescriptor] = Dispatchers.Main
        it[BackgroundDispatcherDescriptor] = Dispatchers.Default
        it[UnconfinedDispatcherDescriptor] = Dispatchers.Unconfined
    }
}

class CoroutineDispatchers(
        val defaultDispatcher: CoroutineDispatcher
) {

    private val dispatchers = mutableMapOf<CoroutineDispatcherDescriptor, CoroutineDispatcher>()

    operator fun set(descriptor: CoroutineDispatcherDescriptor, dispatcher: CoroutineDispatcher) {
        dispatchers[descriptor] = dispatcher
    }

    operator fun get(descriptor: CoroutineDispatcherDescriptor): CoroutineDispatcher {
        return dispatchers[descriptor] ?: defaultDispatcher
    }

}