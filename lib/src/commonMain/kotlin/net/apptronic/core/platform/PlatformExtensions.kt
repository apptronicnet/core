package net.apptronic.core.platform

import net.apptronic.core.threading.WorkerProvider

fun logMessage(text: String) {
    getPlatform().logMessage(text)
}

fun defaultWorkerProvider(): WorkerProvider {
    return getPlatform().defaultWorkerProvider()
}

fun uiWorkerProvider(): WorkerProvider {
    return getPlatform().uiWorkerProvider()
}

fun uiAsyncWorkerProvider(): WorkerProvider {
    return getPlatform().uiAsyncWorkerProvider()
}

fun createSynchronized(): Synchronized {
    return getPlatform().createSynchronized()
}

fun runInNewThread(action: () -> Unit) {
    getPlatform().runInNewThread(action)
}

fun pauseCurrentThread(timeInMillis: Long) {
    getPlatform().pauseCurrentThread(timeInMillis)
}

fun <T> createAtomicReference(initialValue: T): AtomicReference<T> {
    return getPlatform().createAtomicReference(initialValue)
}

