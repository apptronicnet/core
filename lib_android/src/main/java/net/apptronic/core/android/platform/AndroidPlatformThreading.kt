package net.apptronic.core.android.platform

import net.apptronic.core.android.component.AndroidMainThreadWorkerProvider
import net.apptronic.core.component.platform.PlatformThreading
import net.apptronic.core.threading.WorkerProvider

class AndroidPlatformThreading : PlatformThreading {

    override fun runInNewThread(action: () -> Unit) {
        Thread(Runnable {
            action.invoke()
        }).start()
    }

    override fun defaultWorkerProvider(): WorkerProvider {
        return AndroidMainThreadWorkerProvider
    }

    override fun uiWorkerProvider(): WorkerProvider {
        return AndroidMainThreadWorkerProvider
    }

}