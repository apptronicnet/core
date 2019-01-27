package net.apptronic.common.core.mvvm.threading

class SynchronousExecutor : ThreadExecutor {

    override fun execute(action: () -> Unit) {
        action()
    }

}