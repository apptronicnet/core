package net.apptronic.core.component.platform

interface Platform {

    fun platformLogging(): PlatformLogging

    fun platformThreading(): PlatformThreading

}