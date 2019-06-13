package net.apptronic.core.platform

import net.apptronic.core.threading.InstanceWorkerProvider
import net.apptronic.core.threading.WorkerProvider
import net.apptronic.core.threading.synchronousWorker

object TestPlatform : Platform {

    override fun logMessage(text: String) {
        println(text)
    }

    override fun defaultWorkerProvider(): WorkerProvider {
        return InstanceWorkerProvider(synchronousWorker())
    }

    override fun defaultAsyncWorkerProvider(): WorkerProvider {
        return InstanceWorkerProvider(synchronousWorker())
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
        throw UnsupportedOperationException()
    }

}