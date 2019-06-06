package net.apptronic.core.platform

import net.apptronic.core.threading.WorkerProvider

interface Platform {

    fun logMessage(text: String)

    fun defaultWorkerProvider(): WorkerProvider

    fun defaultAsyncWorkerProvider(): WorkerProvider

    fun uiWorkerProvider(): WorkerProvider

    fun uiAsyncWorkerProvider(): WorkerProvider

    fun runInNewThread(action: () -> Unit)

    fun pauseCurrentThread(timeInMillis: Long)

    fun currentTimeInMillis(): Long

    fun elapsedRealtimeMillis(): Long

}