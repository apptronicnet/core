package net.apptronic.core.android.platform

import android.util.Log
import net.apptronic.core.component.platform.PlatformLogging

class AndroidPlatformLogging : PlatformLogging {

    override fun logMessage(text: String) {
        Log.i("apptronic.net_core", text)
    }

}