package net.apptronic.common.android.ui.components.activity

import net.apptronic.common.android.ui.components.android.AndroidLifecycle
import net.apptronic.common.android.ui.threading.ThreadExecutorProvider
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleStage

class ActivityLifecycle(provider: ThreadExecutorProvider) : AndroidLifecycle(provider) {

    companion object {
        const val STAGE_CREATED = AndroidLifecycle.STAGE_CREATED
        const val STAGE_STARTED = AndroidLifecycle.STAGE_STARTED
        const val STAGE_RESUMED = AndroidLifecycle.STAGE_RESUMED
    }

    private val createdStage = createStage(STAGE_CREATED)
    private val startedStage = createStage(STAGE_STARTED)
    private val resumedStage = createStage(STAGE_RESUMED)

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