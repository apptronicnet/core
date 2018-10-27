package net.apptronic.common.android.ui.threading

import android.app.Activity

class AndroidActivityMainThreadExecutor(private val activity: Activity) : ThreadExecutor {

    override fun execute(action: () -> Unit) {
        activity.runOnUiThread(action)
    }

}