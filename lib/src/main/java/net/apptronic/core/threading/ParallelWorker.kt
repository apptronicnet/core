package net.apptronic.core.threading

import kotlin.concurrent.thread

internal class ParallelWorker : Worker {

    override fun run(action: () -> Unit) {
        thread(start = true) {
            action.invoke()
        }
    }

}