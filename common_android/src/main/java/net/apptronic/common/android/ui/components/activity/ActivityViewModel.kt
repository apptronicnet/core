package net.apptronic.common.android.ui.components.activity

import net.apptronic.common.android.ui.viewmodel.ViewModel
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleStage

open class ActivityViewModel(lifecycleHolder: LifecycleHolder<ActivityLifecycle>) : ViewModel(lifecycleHolder) {

    private val lifecycle: ActivityLifecycle = lifecycleHolder.localLifecycle()

    /**
     * Do single action on stage is created
     */
    fun onceCreated(key: String, action: () -> Unit) {
        lifecycle.createdStage.doOnce(key, action)
    }

    /**
     * Do single action on stage is started
     */
    fun onceStarted(key: String, action: () -> Unit) {
        lifecycle.startedStage.doOnce(key, action)
    }

    /**
     * Do single action on stage is resumed
     */
    fun onceResumed(key: String, action: () -> Unit) {
        lifecycle.resumedStage.doOnce(key, action)
    }

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