package net.apptronic.common.core.base.threading

class SynchronousWorker : Worker {

    override fun run(action: () -> Unit) {
        action()
    }

}