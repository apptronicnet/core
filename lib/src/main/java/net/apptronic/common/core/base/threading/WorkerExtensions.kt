package net.apptronic.common.core.base.threading

private val defaultWorkerInstance = SynchronousWorker()

fun defaultWorker(): Worker {
    return defaultWorkerInstance
}