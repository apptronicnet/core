package net.apptronic.common.android.ui.threading

interface ThreadExecutorProvider {

    fun provideThreadExecutor(): ThreadExecutor

}