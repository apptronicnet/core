package net.apptronic.common.android.ui.threading

interface ThreadExecutor : ThreadExecutorProvider {

    override fun provideThreadExecutor(): ThreadExecutor {
        return this
    }

    fun execute(action: () -> Unit)

}