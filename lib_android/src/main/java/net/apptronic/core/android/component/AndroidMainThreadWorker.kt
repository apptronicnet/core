package net.apptronic.core.android.component

import android.os.Handler
import android.os.Looper
import net.apptronic.core.threading.Worker

object AndroidMainThreadWorker : Worker {

    private val handler = Handler(Looper.getMainLooper())

    override fun run(action: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action.invoke()
        } else {
            handler.post {
                action.invoke()
            }
        }
    }

}