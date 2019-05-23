package net.apptronic.core.component.platform

import net.apptronic.core.threading.WorkerProvider

interface PlatformThreading {

    fun defaultWorkerProvider(): WorkerProvider

    fun uiWorkerProvider(): WorkerProvider

    fun uiAsyncWorkerProvider(): WorkerProvider

}