package net.apptronic.common.android.ui.components

import net.apptronic.common.android.ui.viewmodel.ViewModel
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleStage

open class FragmentViewModel(lifecycleHolder: LifecycleHolder<FragmentLifecycle>) : ViewModel(lifecycleHolder) {

    private val lifecycle: FragmentLifecycle = lifecycleHolder.localLifecycle()

    fun onCreate(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        lifecycle.createdStage.subscribeEnter(callback)
    }

    fun onViewCreated(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        lifecycle.viewCreatedStage.subscribeEnter(callback)
    }

    fun onStart(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        lifecycle.startedStage.subscribeEnter(callback)
    }

    fun onResume(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        lifecycle.resumedStage.subscribeEnter(callback)
    }

    fun onPause(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        lifecycle.resumedStage.subscribeExit(callback)
    }

    fun onStop(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        lifecycle.startedStage.subscribeExit(callback)
    }

    fun onDestroyView(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        lifecycle.viewCreatedStage.subscribeExit(callback)
    }

    fun onDestroy(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        lifecycle.createdStage.subscribeExit(callback)
    }

}