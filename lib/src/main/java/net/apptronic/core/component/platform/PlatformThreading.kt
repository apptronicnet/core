package net.apptronic.core.component.platform

import net.apptronic.core.threading.WorkerProvider

interface PlatformThreading {

    fun runInNewThread(action: () -> Unit)

    fun defaultWorkerProvider(): WorkerProvider

    fun uiWorkerProvider(): WorkerProvider

    fun suspendCurrentThread(timeInMillis: Long)

}