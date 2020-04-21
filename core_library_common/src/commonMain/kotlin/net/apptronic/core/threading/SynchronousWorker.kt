package net.apptronic.core.threading

@Deprecated("Should use coroutines")
class SynchronousWorker : Worker {

    override fun execute(action: Action) {
        action.execute()
    }

}