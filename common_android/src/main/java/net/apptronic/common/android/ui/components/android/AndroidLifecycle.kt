package net.apptronic.common.android.ui.components.android

import net.apptronic.common.android.ui.threading.ThreadExecutorProvider
import net.apptronic.common.android.ui.viewmodel.lifecycle.Lifecycle
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleStage

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