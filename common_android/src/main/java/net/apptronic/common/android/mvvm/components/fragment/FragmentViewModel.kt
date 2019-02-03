package net.apptronic.common.android.mvvm.components.fragment

import net.apptronic.common.android.mvvm.components.activity.AndroidViewModel
import net.apptronic.common.core.component.lifecycle.Lifecycle
import net.apptronic.common.core.component.lifecycle.LifecycleStage

open class FragmentViewModel(lifecycle: Lifecycle) : AndroidViewModel(lifecycle) {

    fun doOnceViewCreated(key: String, action: () -> Unit) {
        onceStage(FragmentLifecycle.STAGE_VIEW_CREATED, key, action)
    }

    fun doOnViewCreated(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(FragmentLifecycle.STAGE_VIEW_CREATED, callback)
    }

    fun doOnDestroyView(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(FragmentLifecycle.STAGE_VIEW_CREATED, callback)
    }


}