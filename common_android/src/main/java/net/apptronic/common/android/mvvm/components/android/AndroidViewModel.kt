package net.apptronic.common.android.mvvm.components.activity

import net.apptronic.common.android.mvvm.components.android.AndroidLifecycle
import net.apptronic.common.core.mvvm.viewmodel.ViewModel
import net.apptronic.common.core.mvvm.viewmodel.lifecycle.Lifecycle
import net.apptronic.common.core.mvvm.viewmodel.lifecycle.LifecycleStage

open class AndroidViewModel(lifecycle: Lifecycle) : ViewModel(lifecycle) {

    /**
     * Do single action on stage is created
     */
    fun doOnceCreated(key: String, action: () -> Unit) {
        onceStage(AndroidLifecycle.STAGE_CREATED, key, action)
    }

    /**
     * Do single action on stage is started
     */
    fun doOnceStarted(key: String, action: () -> Unit) {
        onceStage(AndroidLifecycle.STAGE_STARTED, key, action)
    }

    /**
     * Do single action on stage is resumed
     */
    fun doOnceResumed(key: String, action: () -> Unit) {
        onceStage(AndroidLifecycle.STAGE_RESUMED, key, action)
    }

    fun doOnCreate(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(AndroidLifecycle.STAGE_CREATED, callback)
    }

    fun doOnStart(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(AndroidLifecycle.STAGE_STARTED, callback)
    }

    fun doOnResume(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        onEnterStage(AndroidLifecycle.STAGE_RESUMED, callback)
    }

    fun doOnPause(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(AndroidLifecycle.STAGE_RESUMED, callback)
    }

    fun doOnStop(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(AndroidLifecycle.STAGE_STARTED, callback)
    }

    fun doOnDestroy(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(AndroidLifecycle.STAGE_CREATED, callback)
    }

}