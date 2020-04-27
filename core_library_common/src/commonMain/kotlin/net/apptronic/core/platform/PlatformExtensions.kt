package net.apptronic.core.platform

fun logMessage(text: String) {
    getPlatform().logMessage(text)
}

fun currentTimeInMillis(): Long {
    return getPlatform().currentTimeInMillis()
}

fun elapsedRealtimeMillis(): Long {
    return getPlatform().elapsedRealtimeMillis()
}

