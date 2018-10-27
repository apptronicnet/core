package net.apptronic.common.android.ui.threading

import android.app.Activity

class SynchronousExecutor(private val activity: Activity) : ThreadExecutor {

    override fun execute(action: () -> Unit) {
        action()
    }

}