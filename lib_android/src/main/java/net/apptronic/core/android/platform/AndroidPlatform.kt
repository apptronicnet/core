package net.apptronic.core.android.platform

import android.os.SystemClock
import android.util.Log
import net.apptronic.core.android.component.AndroidAsyncMainThreadWorkerProvider
import net.apptronic.core.android.component.AndroidMainThreadWorkerProvider
import net.apptronic.core.platform.Platform
import net.apptronic.core.threading.WorkerProvider

object AndroidPlatform : Platform {

    override fun logMessage(text: String) {
        Log.i("apptronic.net_core", text)
    }

    override fun defaultWorkerProvider(): WorkerProvider {
        return AndroidMainThreadWorkerProvider
    }

    override fun defaultAsyncWorkerProvider(): WorkerProvider {
        return AndroidAsyncMainThreadWorkerProvider
    }

    override fun uiWorkerProvider(): WorkerProvider {
        return AndroidMainThreadWorkerProvider
    }

    override fun uiAsyncWorkerProvider(): WorkerProvider {
        return AndroidAsyncMainThreadWorkerProvider
    }

    override fun runInNewThread(action: () -> Unit) {
        Thread(Runnable { action.invoke() }).start()
    }

    override fun pauseCurrentThread(timeInMillis: Long) {
        val obj = java.lang.Object()
        synchronized(obj) {
            try {
                obj.wait(timeInMillis);
            } catch (e: InterruptedException) {
                // ignore
            }
        }
    }

    override fun currentTimeInMillis(): Long {
        return System.currentTimeMillis()
    }

    override fun elapsedRealtimeMillis(): Long {
        return SystemClock.elapsedRealtime()
    }

}