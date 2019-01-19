package net.apptronic.common.android.ui.threading

import android.os.Handler
import android.os.Looper

class AndroidMainThreadExecutor : ThreadExecutor {

    private val handler = Handler(Looper.getMainLooper())

    override fun execute(action: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action()
        } else {
            handler.post(action)
        }
    }

}