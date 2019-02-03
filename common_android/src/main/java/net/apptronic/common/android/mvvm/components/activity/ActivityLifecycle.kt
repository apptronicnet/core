package net.apptronic.common.android.mvvm.components.activity

import net.apptronic.common.android.mvvm.components.android.AndroidLifecycle
import net.apptronic.common.core.component.lifecycle.LifecycleStageImpl

class ActivityLifecycle(provider: ThreadExecutorProvider) : AndroidLifecycle(provider) {

    companion object {
        const val STAGE_CREATED = AndroidLifecycle.STAGE_CREATED
        const val STAGE_STARTED = AndroidLifecycle.STAGE_STARTED
        const val STAGE_RESUMED = AndroidLifecycle.STAGE_RESUMED
    }

    private val createdStage = createStage(STAGE_CREATED)
    private val startedStage = createStage(STAGE_STARTED)
    private val resumedStage = createStage(STAGE_RESUMED)

    override fun getCreatedStage(): LifecycleStageImpl {
        return createdStage
    }

    override fun getStartedStage(): LifecycleStageImpl {
        return startedStage
    }

    override fun getResumedStage(): LifecycleStageImpl {
        return resumedStage
    }

}