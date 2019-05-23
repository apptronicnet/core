package net.apptronic.core.android.platform

import net.apptronic.core.android.component.AndroidAsyncMainThreadWorkerProvider
import net.apptronic.core.android.component.AndroidMainThreadWorkerProvider
import net.apptronic.core.component.platform.PlatformThreading
import net.apptronic.core.threading.WorkerProvider

class AndroidPlatformThreading : PlatformThreading {

    private val javaUtils = JavaUtils()

    override fun defaultWorkerProvider(): WorkerProvider {
        return AndroidMainThreadWorkerProvider
    }

    override fun uiWorkerProvider(): WorkerProvider {
        return AndroidMainThreadWorkerProvider
    }

    override fun uiAsyncWorkerProvider(): WorkerProvider {
        return AndroidAsyncMainThreadWorkerProvider
    }

    override fun suspendCurrentThread(timeInMillis: Long) {
        javaUtils.suspendCurrentThread(timeInMillis)
    }

}