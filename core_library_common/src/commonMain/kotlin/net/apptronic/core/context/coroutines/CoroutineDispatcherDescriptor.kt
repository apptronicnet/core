package net.apptronic.core.context.coroutines

fun coroutineDispatcherDescriptor(name: String): CoroutineDispatcherDescriptor {
    return CoroutineDispatcherDescriptor(name)
}

class CoroutineDispatcherDescriptor internal constructor(val name: String)