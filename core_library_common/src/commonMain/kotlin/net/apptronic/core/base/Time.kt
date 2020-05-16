package net.apptronic.core.base

/**
 * Get some timestamp value which allows to measure time difference based on returned value. Does not require to return
 * real timestamp but requires to be independent of system time changes.
 */
expect fun elapsedRealtimeMillis(): Long

expect fun elapsedRealtimeMicros(): Long

expect fun elapsedRealtimeNano(): Long