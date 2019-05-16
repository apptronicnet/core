package net.apptronic.core.android.component

import android.os.Handler
import android.os.Looper
import net.apptronic.core.threading.Action
import net.apptronic.core.threading.InstanceWorkerProvider
import net.apptronic.core.threading.Worker

val AndroidMainThreadWorkerProvider = InstanceWorkerProvider(AndroidMainThreadWorker)

private object AndroidMainThreadWorker : Worker {

    private val handler = Handler(Looper.getMainLooper())

    override fun execute(action: Action) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action.execute()
        } else {
            handler.post {
                action.execute()
            }
        }
    }

}