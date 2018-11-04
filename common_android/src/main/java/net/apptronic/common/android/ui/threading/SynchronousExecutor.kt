package net.apptronic.common.android.ui.threading

class SynchronousExecutor : ThreadExecutor {

    override fun execute(action: () -> Unit) {
        action()
    }

}