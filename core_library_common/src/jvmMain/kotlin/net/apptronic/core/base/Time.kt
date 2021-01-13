package net.apptronic.core.base

private var nanoTimeProvider: NanoTimeProvider = SystemNanoTimeProvider()

actual fun elapsedRealtimeMillis(): Long {
    return nanoTimeProvider.nanoTime() / 1000000L
}

actual fun elapsedRealtimeMicros(): Long {
    return nanoTimeProvider.nanoTime() / 1000L
}

actual fun elapsedRealtimeNano(): Long {
    return nanoTimeProvider.nanoTime()
}

interface NanoTimeProvider {

    fun nanoTime(): Long

}

private class SystemNanoTimeProvider : NanoTimeProvider {

    override fun nanoTime(): Long {
        return System.nanoTime()
    }

}
