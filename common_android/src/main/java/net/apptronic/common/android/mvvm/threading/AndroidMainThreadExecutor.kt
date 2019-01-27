package net.apptronic.common.android.mvvm.threading

import android.os.Handler
import android.os.Looper
import net.apptronic.common.core.mvvm.threading.ThreadExecutor

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