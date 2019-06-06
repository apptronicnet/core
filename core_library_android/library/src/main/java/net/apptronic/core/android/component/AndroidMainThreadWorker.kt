package net.apptronic.core.android.component

import android.os.Handler
import android.os.Looper
import net.apptronic.core.threading.Action
import net.apptronic.core.threading.InstanceWorkerProvider
import net.apptronic.core.threading.Worker

val AndroidMainThreadWorkerProvider = InstanceWorkerProvider(AndroidMainThreadWorker)
val AndroidAsyncMainThreadWorkerProvider = InstanceWorkerProvider(AndroidAsyncMainThreadWorker)

private val SHARED_HANDLER = Handler(Looper.getMainLooper())

private object AndroidMainThreadWorker : Worker {

    override fun execute(action: Action) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action.execute()
        } else {
            SHARED_HANDLER.post {
                action.execute()
            }
        }
    }

}


private object AndroidAsyncMainThreadWorker : Worker {

    override fun execute(action: Action) {
        SHARED_HANDLER.post {
            action.execute()
        }
    }

}