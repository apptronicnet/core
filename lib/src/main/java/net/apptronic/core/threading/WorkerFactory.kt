package net.apptronic.core.threading

import net.apptronic.core.component.platform.PlatformHandler

fun serialWorker(platformHandler: PlatformHandler): Worker {
    return SerialWorker(platformHandler, 1)
}

private val synchronousWorkerInstance = SynchronousWorker()

fun synchronousWorker(): Worker {
    return synchronousWorkerInstance
}

/**
 * Worker which executes actions in single thread.
 */
fun singleThreadWorker(platformHandler: PlatformHandler): Worker {
    return QueueWorker(platformHandler, 1)
}

fun parallelWorker(platformHandler: PlatformHandler, threads: Int = 16): Worker {
    return QueueWorker(platformHandler, threads)
}