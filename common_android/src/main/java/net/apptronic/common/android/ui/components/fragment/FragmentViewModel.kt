package net.apptronic.common.android.ui.components.fragment

import net.apptronic.common.android.ui.components.activity.AndroidViewModel
import net.apptronic.common.android.ui.viewmodel.lifecycle.Lifecycle
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleStage

open class FragmentViewModel(lifecycle: Lifecycle) : AndroidViewModel(lifecycle) {

    fun onceViewCreated(key: String, action: () -> Unit) {
        onceStage(FragmentLifecycle.STAGE_VIEW_CREATED, key, action)
    }

    fun onViewCreated(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(FragmentLifecycle.STAGE_VIEW_CREATED, callback)
    }

    fun onDestroyView(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(FragmentLifecycle.STAGE_VIEW_CREATED, callback)
    }


}