package net.apptronic.core.platform

actual fun runInNewThread(action: () -> Unit) {
    Thread(Runnable { action.invoke() }).start()
}

actual fun pauseCurrentThread(timeInMillis: Long) {
    val obj = java.lang.Object()
    synchronized(obj) {
        try {
            obj.wait(timeInMillis);
        } catch (e: InterruptedException) {
            // ignore
        }
    }
}