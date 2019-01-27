package net.apptronic.common.core.mvvm.threading

interface ThreadExecutor : ThreadExecutorProvider {

    override fun provideThreadExecutor(): ThreadExecutor {
        return this
    }

    fun execute(action: () -> Unit)

}