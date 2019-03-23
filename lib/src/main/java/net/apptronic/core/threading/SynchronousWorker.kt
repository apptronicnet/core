package net.apptronic.core.threading

class SynchronousWorker : Worker {

    override fun run(action: () -> Unit) {
        action()
    }

}