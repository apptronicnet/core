package net.apptronic.core.threading

private val defaultWorkerInstance = SynchronousWorker()

fun defaultWorker(): Worker {
    return defaultWorkerInstance
}