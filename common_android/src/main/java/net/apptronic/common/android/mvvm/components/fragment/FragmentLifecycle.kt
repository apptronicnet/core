package net.apptronic.common.android.mvvm.components.fragment

import net.apptronic.common.android.mvvm.components.android.AndroidLifecycle
import net.apptronic.common.core.mvvm.threading.ThreadExecutorProvider
import net.apptronic.common.core.mvvm.viewmodel.lifecycle.LifecycleStage

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

    override fun getCreatedStage(): LifecycleStage {
        return createdStage
    }

    fun getViewCreatedStage(): LifecycleStage {
        return viewCreatedStage
    }

    override fun getStartedStage(): LifecycleStage {
        return startedStage
    }

    override fun getResumedStage(): LifecycleStage {
        return resumedStage
    }


}