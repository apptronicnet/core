package net.apptronic.common.android.ui.components.activity

import net.apptronic.common.android.ui.components.android.AndroidLifecycle
import net.apptronic.common.android.ui.viewmodel.ViewModel
import net.apptronic.common.android.ui.viewmodel.lifecycle.Lifecycle
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleStage

open class AndroidViewModel(lifecycle: Lifecycle) : ViewModel(lifecycle) {

    /**
     * Do single action on stage is created
     */
    fun onceCreated(key: String, action: () -> Unit) {
        onceStage(AndroidLifecycle.STAGE_CREATED, key, action)
    }

    /**
     * Do single action on stage is started
     */
    fun onceStarted(key: String, action: () -> Unit) {
        onceStage(AndroidLifecycle.STAGE_STARTED, key, action)
    }

    /**
     * Do single action on stage is resumed
     */
    fun onceResumed(key: String, action: () -> Unit) {
        onceStage(AndroidLifecycle.STAGE_RESUMED, key, action)
    }

    fun onCreate(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(AndroidLifecycle.STAGE_CREATED, callback)
    }

    fun onStart(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(AndroidLifecycle.STAGE_STARTED, callback)
    }

    fun onResume(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(AndroidLifecycle.STAGE_RESUMED, callback)
    }

    fun onPause(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(AndroidLifecycle.STAGE_RESUMED, callback)
    }

    fun onStop(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(AndroidLifecycle.STAGE_STARTED, callback)
    }

    fun onDestroy(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(AndroidLifecycle.STAGE_CREATED, callback)
    }

}