package net.apptronic.core.android.platform

import android.os.SystemClock
import android.util.Log
import net.apptronic.core.platform.Platform

object AndroidPlatform : Platform {

    override fun logMessage(text: String) {
        Log.i("apptronic.net_core", text)
    }

    override fun currentTimeInMillis(): Long {
        return System.currentTimeMillis()
    }

    override fun elapsedRealtimeMillis(): Long {
        return SystemClock.elapsedRealtime()
    }

}