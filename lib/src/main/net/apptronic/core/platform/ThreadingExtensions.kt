package net.apptronic.core.platform

/**
 * Launch action in new thread
 */
expect fun runInNewThread(action: () -> Unit)

/**
 * Pause current thread for specific time
 */
expect fun pauseCurrentThread(timeInMillis: Long)
