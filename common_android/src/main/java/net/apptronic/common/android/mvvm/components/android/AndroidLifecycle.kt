package net.apptronic.common.android.mvvm.components.android

import net.apptronic.common.core.mvvm.threading.ThreadExecutorProvider
import net.apptronic.common.core.mvvm.viewmodel.lifecycle.Lifecycle
import net.apptronic.common.core.mvvm.viewmodel.lifecycle.LifecycleStage

abstract class AndroidLifecycle(provider: ThreadExecutorProvider) : Lifecycle(provider) {

    companion object {
        const val STAGE_CREATED = "android:created"
        const val STAGE_STARTED = "android:started"
        const val STAGE_RESUMED = "android:resumed"
    }

    abstract fun getCreatedStage(): LifecycleStage
    abstract fun getStartedStage(): LifecycleStage
    abstract fun getResumedStage(): LifecycleStage

}