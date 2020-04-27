package net.apptronic.core.platform

interface Platform {

    fun logMessage(text: String)

    fun currentTimeInMillis(): Long

    fun elapsedRealtimeMillis(): Long

}