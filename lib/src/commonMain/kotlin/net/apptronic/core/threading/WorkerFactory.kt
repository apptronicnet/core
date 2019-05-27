package net.apptronic.core.threading

fun serialWorker(): Worker {
    return SerialWorker(1)
}

private val synchronousWorkerInstance = SynchronousWorker()

fun synchronousWorker(): Worker {
    return synchronousWorkerInstance
}

/**
 * Worker which executes actions in single thread.
 */
fun singleThreadWorker(): Worker {
    return QueueWorker(1)
}

fun parallelWorker(threads: Int = 16): Worker {
    return QueueWorker(threads)
}