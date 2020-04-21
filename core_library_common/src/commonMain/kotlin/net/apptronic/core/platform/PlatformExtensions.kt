package net.apptronic.core.platform

import net.apptronic.core.threading.WorkerProvider

fun logMessage(text: String) {
    getPlatform().logMessage(text)
}

fun defaultWorkerProvider(): WorkerProvider {
    return getPlatform().defaultWorkerProvider()
}

@Deprecated("Coroutines")
fun runInNewThread(action: () -> Unit) {
    getPlatform().runInNewThread(action)
}

@Deprecated("Coroutines")
fun pauseCurrentThread(timeInMillis: Long) {
    getPlatform().pauseCurrentThread(timeInMillis)
}

fun currentTimeInMillis(): Long {
    return getPlatform().currentTimeInMillis()
}

fun elapsedRealtimeMillis(): Long {
    return getPlatform().elapsedRealtimeMillis()
}

