package net.apptronic.core.platform

import net.apptronic.core.base.concurrent.Volatile
import net.apptronic.core.platform.PlatformInstance.platform

fun initializePlatform(platform: Platform) {
    PlatformInstance.initialize(platform)
}

private object PlatformInstance {

    private val platformReference = Volatile<Platform?>(null)

    val platform by lazy {
        platformReference.get()!!
    }

    fun initialize(platform: Platform) {
        this.platformReference.set(platform)
    }

}

internal fun getPlatform(): Platform {
    return platform
}