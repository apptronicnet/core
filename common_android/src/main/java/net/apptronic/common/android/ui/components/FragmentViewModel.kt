package net.apptronic.common.android.ui.components

import net.apptronic.common.android.ui.viewmodel.ViewModel
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

open class FragmentViewModel(lifecycleHolder: LifecycleHolder<FragmentLifecycle>) : ViewModel(lifecycleHolder) {

    private val lifecycle: FragmentLifecycle = lifecycleHolder.localLifecycle()

    fun onCreate(callback: () -> Unit) {
        lifecycle.createdStage.subscribeEnter(callback)
    }

    fun onViewCreated(callback: () -> Unit) {
        lifecycle.viewCreatedStage.subscribeEnter(callback)
    }

    fun onStart(callback: () -> Unit) {
        lifecycle.startedStage.subscribeEnter(callback)
    }

    fun onResume(callback: () -> Unit) {
        lifecycle.resumedStage.subscribeEnter(callback)
    }

    fun onPause(callback: () -> Unit) {
        lifecycle.resumedStage.subscribeExit(callback)
    }

    fun onStop(callback: () -> Unit) {
        lifecycle.startedStage.subscribeExit(callback)
    }

    fun onDestroyView(callback: () -> Unit) {
        lifecycle.viewCreatedStage.subscribeExit(callback)
    }

    fun onDestroy(callback: () -> Unit) {
        lifecycle.createdStage.subscribeExit(callback)
    }

}