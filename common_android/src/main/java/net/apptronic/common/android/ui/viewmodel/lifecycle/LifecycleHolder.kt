package net.apptronic.common.android.ui.viewmodel.lifecycle

import net.apptronic.common.android.ui.threading.ThreadExecutor
import net.apptronic.common.android.ui.threading.ThreadExecutorProvider

interface LifecycleHolder : ThreadExecutorProvider {

    override fun provideThreadExecutor(): ThreadExecutor {
        return getLifecycle().provideThreadExecutor()
    }

    fun getLifecycle(): Lifecycle

}