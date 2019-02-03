package net.apptronic.common.core.base.threading

fun serialWorker(): Worker {
    return SerialWorker()
}

fun synchronousWorker(): Worker {
    return SynchronousWorker()
}

fun singleWorker(): Worker {
    return SerialWorker(1)
}

fun parallelWorker(): Worker {
    return ParallelWorker()
}