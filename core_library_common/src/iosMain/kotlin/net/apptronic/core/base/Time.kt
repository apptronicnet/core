package net.apptronic.core.base

import kotlin.system.getTimeMicros
import kotlin.system.getTimeMillis
import kotlin.system.getTimeNanos

actual fun elapsedRealtimeMillis(): Long {
    return getTimeMillis()
}

actual fun elapsedRealtimeMicros(): Long {
    return getTimeMicros()
}

actual fun elapsedRealtimeNano(): Long {
    return getTimeNanos()
}