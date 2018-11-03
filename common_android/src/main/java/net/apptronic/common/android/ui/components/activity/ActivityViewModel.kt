package net.apptronic.common.android.ui.components.activity

import net.apptronic.common.android.ui.viewmodel.ViewModel
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleStage

open class ActivityViewModel(lifecycleHolder: LifecycleHolder<ActivityLifecycle>) : ViewModel(lifecycleHolder) {

    private val lifecycle: ActivityLifecycle = lifecycleHolder.localLifecycle()

    fun onCreate(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        lifecycle.createdStage.doOnEnter(callback)
    }

    fun onStart(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        lifecycle.startedStage.doOnEnter(callback)
    }

    fun onResume(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        lifecycle.resumedStage.doOnEnter(callback)
    }

    fun onPause(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        lifecycle.resumedStage.doOnExit(callback)
    }

    fun onStop(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        lifecycle.startedStage.doOnExit(callback)
    }

    fun onDestroy(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        lifecycle.createdStage.doOnExit(callback)
    }

}