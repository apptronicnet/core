package net.apptronic.core.platform

import net.apptronic.core.platform.PlatformInstance.platform

fun initializePlatform(platform: Platform) {
    PlatformInstance.initialize(platform)
}

private object PlatformInstance {

    private lateinit var platformReference: Platform

    val platform by lazy {
        platformReference
    }

    fun initialize(platform: Platform) {
        this.platformReference = platform
    }

}

internal fun getPlatform(): Platform {
    return platform
}