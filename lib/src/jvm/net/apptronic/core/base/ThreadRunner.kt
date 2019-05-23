package net.apptronic.core.base

actual fun runInNewThread(action: () -> Unit) {
    Thread(Runnable { action.invoke() }).start()
}