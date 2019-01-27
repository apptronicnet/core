package net.apptronic.common.core.mvvm.threading

interface ThreadExecutorProvider {

    fun provideThreadExecutor(): ThreadExecutor

}