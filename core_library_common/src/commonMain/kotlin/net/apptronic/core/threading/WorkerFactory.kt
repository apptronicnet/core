package net.apptronic.core.threading

@Deprecated("Should use coroutines")
fun serialWorker(): Worker {
    return SerialWorker(1)
}

@Deprecated("Should use coroutines")
private val synchronousWorkerInstance = SynchronousWorker()

@Deprecated("Should use coroutines")
fun synchronousWorker(): Worker {
    return synchronousWorkerInstance
}

/**
 * Worker which executes actions in single thread.
 */
@Deprecated("Should use coroutines")
fun singleThreadWorker(): Worker {
    return QueueWorker(1)
}

@Deprecated("Should use coroutines")
fun parallelWorker(threads: Int = 16): Worker {
    return QueueWorker(threads)
}