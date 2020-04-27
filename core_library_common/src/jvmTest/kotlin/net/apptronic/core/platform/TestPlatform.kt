package net.apptronic.core.platform

object TestPlatform : Platform {

    override fun logMessage(text: String) {
        println(text)
    }

    override fun currentTimeInMillis(): Long {
        return System.currentTimeMillis()
    }

    override fun elapsedRealtimeMillis(): Long {
        throw UnsupportedOperationException()
    }

}