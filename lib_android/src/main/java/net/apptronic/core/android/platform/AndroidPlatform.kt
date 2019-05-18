package net.apptronic.core.android.platform

import net.apptronic.core.component.platform.Platform
import net.apptronic.core.component.platform.PlatformLogging
import net.apptronic.core.component.platform.PlatformThreading

class AndroidPlatform : Platform {

    override fun platformLogging(): PlatformLogging {
        return AndroidPlatformLogging()
    }

    override fun platformThreading(): PlatformThreading {
        return AndroidPlatformThreading()
    }

}