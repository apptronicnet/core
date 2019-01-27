package net.apptronic.common.core.mvvm.viewmodel.lifecycle

import net.apptronic.common.core.mvvm.threading.ThreadExecutor
import net.apptronic.common.core.mvvm.threading.ThreadExecutorProvider

interface LifecycleHolder : ThreadExecutorProvider {

    override fun provideThreadExecutor(): ThreadExecutor {
        return getLifecycle().provideThreadExecutor()
    }

    fun getLifecycle(): Lifecycle

}