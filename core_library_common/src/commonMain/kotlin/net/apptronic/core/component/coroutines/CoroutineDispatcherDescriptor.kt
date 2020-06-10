package net.apptronic.core.component.coroutines

fun coroutineDispatcherDescriptor(name: String): CoroutineDispatcherDescriptor {
    return CoroutineDispatcherDescriptor(name)
}

class CoroutineDispatcherDescriptor internal constructor(val name: String)