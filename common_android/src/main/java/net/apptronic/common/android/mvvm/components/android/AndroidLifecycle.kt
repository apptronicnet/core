package net.apptronic.common.android.mvvm.components.android

import net.apptronic.common.core.component.lifecycle.Lifecycle
import net.apptronic.common.core.component.lifecycle.LifecycleStageImpl

abstract class AndroidLifecycle(provider: ThreadExecutorProvider) : Lifecycle(provider) {

    companion object {
        const val STAGE_CREATED = "android:created"
        const val STAGE_STARTED = "android:started"
        const val STAGE_RESUMED = "android:resumed"
    }

    abstract fun getCreatedStage(): LifecycleStageImpl
    abstract fun getStartedStage(): LifecycleStageImpl
    abstract fun getResumedStage(): LifecycleStageImpl

}