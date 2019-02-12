package net.apptronic.common.android.mvvm.components.activity

import net.apptronic.common.android.mvvm.components.android.AndroidLifecycle
import net.apptronic.common.core.component.lifecycle.LifecycleStage
import net.apptronic.common.core.component.threading.ContextWorkers

class ActivityLifecycle(workers: ContextWorkers) : AndroidLifecycle(workers) {

    companion object {
        const val STAGE_CREATED = AndroidLifecycle.STAGE_CREATED
        const val STAGE_STARTED = AndroidLifecycle.STAGE_STARTED
        const val STAGE_RESUMED = AndroidLifecycle.STAGE_RESUMED
    }

    private val createdStage = addStage(STAGE_CREATED)
    private val startedStage = addStage(STAGE_STARTED)
    private val resumedStage = addStage(STAGE_RESUMED)

    override fun getCreatedStage(): LifecycleStage {
        return createdStage
    }

    override fun getStartedStage(): LifecycleStage {
        return startedStage
    }

    override fun getResumedStage(): LifecycleStage {
        return resumedStage
    }

}