package net.apptronic.core.component.platform

import net.apptronic.core.threading.WorkerProvider

class PlatformHandler(
    platform: Platform
) {

    private val platformThreading = platform.platformThreading()
    private val platformLogging = platform.platformLogging()
    private val defaultWorkerProvider = platformThreading.defaultWorkerProvider()
    private val uiWorkerProvider = platformThreading.uiWorkerProvider()

    fun defaultWorkerProvider(): WorkerProvider {
        return defaultWorkerProvider
    }

    fun uiWorkerProvider(): WorkerProvider {
        return uiWorkerProvider
    }

    fun runInNewThread(action: () -> Unit) {
        platformThreading.runInNewThread(action)
    }

    fun logMessage(text: String) {
        platformLogging.logMessage(text)
    }

    fun suspendCurrentThread(timeInMillis: Long) {
        platformThreading.suspendCurrentThread(timeInMillis)
    }

}