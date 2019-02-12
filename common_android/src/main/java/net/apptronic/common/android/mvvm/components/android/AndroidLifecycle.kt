package net.apptronic.common.android.mvvm.components.android

import net.apptronic.common.core.component.lifecycle.Lifecycle
import net.apptronic.common.core.component.lifecycle.LifecycleStage
import net.apptronic.common.core.component.threading.ContextWorkers

abstract class AndroidLifecycle(
    workers: ContextWorkers
) : Lifecycle(workers) {

    companion object {
        const val STAGE_CREATED = "android:created"
        const val STAGE_STARTED = "android:started"
        const val STAGE_RESUMED = "android:resumed"
    }

    abstract fun getCreatedStage(): LifecycleStage
    abstract fun getStartedStage(): LifecycleStage
    abstract fun getResumedStage(): LifecycleStage

}