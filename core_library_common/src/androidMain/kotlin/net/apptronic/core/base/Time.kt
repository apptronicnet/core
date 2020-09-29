package net.apptronic.core.base

actual fun elapsedRealtimeMillis(): Long {
    return System.nanoTime() / 1000000L
}

actual fun elapsedRealtimeMicros(): Long {
    return System.nanoTime() / 1000L
}

actual fun elapsedRealtimeNano(): Long {
    return System.nanoTime()
}