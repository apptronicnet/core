package net.apptronic.core.platform

import net.apptronic.core.threading.WorkerProvider

interface Platform {

    fun logMessage(text: String)

    fun defaultWorkerProvider(): WorkerProvider

    fun uiWorkerProvider(): WorkerProvider

    fun uiAsyncWorkerProvider(): WorkerProvider

    fun createSynchronized(): Synchronized

    fun runInNewThread(action: () -> Unit)

    fun pauseCurrentThread(timeInMillis: Long)

    fun <T> createAtomicReference(initialValue: T): AtomicReference<T>

}