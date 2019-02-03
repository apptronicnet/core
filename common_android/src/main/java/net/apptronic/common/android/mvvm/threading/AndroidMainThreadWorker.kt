package net.apptronic.common.android.mvvm.threading

import android.os.Handler
import android.os.Looper
import net.apptronic.common.core.base.threading.Worker

object AndroidMainThreadWorker : Worker {

    private val handler = Handler(Looper.getMainLooper())

    override fun run(action: () -> Unit) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            action()
        } else {
            handler.post(action)
        }
    }

}