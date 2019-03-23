package net.apptronic.core.threading

fun serialWorker(): Worker {
    return SerialWorker()
}

fun synchronousWorker(): Worker {
    return SynchronousWorker()
}

/**
 * Worker which executes actions in single thread.
 */
fun singleWorker(): Worker {
    return SerialWorker(1)
}

fun parallelWorker(): Worker {
    return ParallelWorker()
}