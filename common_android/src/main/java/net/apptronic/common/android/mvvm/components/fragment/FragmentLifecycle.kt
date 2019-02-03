package net.apptronic.common.android.mvvm.components.fragment

import net.apptronic.common.android.mvvm.components.android.AndroidLifecycle
import net.apptronic.common.core.component.lifecycle.LifecycleStageImpl

class FragmentLifecycle(provider: ThreadExecutorProvider) : AndroidLifecycle(provider) {

    companion object {
        const val STAGE_CREATED = AndroidLifecycle.STAGE_CREATED
        const val STAGE_VIEW_CREATED = "android:view_created"
        const val STAGE_STARTED = AndroidLifecycle.STAGE_STARTED
        const val STAGE_RESUMED = AndroidLifecycle.STAGE_RESUMED
    }

    private val createdStage = createStage(STAGE_CREATED)
    private val viewCreatedStage = createStage(STAGE_VIEW_CREATED)
    private val startedStage = createStage(STAGE_STARTED)
    private val resumedStage = createStage(STAGE_RESUMED)

    override fun getCreatedStage(): LifecycleStageImpl {
        return createdStage
    }

    fun getViewCreatedStage(): LifecycleStageImpl {
        return viewCreatedStage
    }

    override fun getStartedStage(): LifecycleStageImpl {
        return startedStage
    }

    override fun getResumedStage(): LifecycleStageImpl {
        return resumedStage
    }


}