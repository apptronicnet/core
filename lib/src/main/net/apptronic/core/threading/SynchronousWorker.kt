package net.apptronic.core.threading

class SynchronousWorker : Worker {

    override fun execute(action: Action) {
        action.execute()
    }

}