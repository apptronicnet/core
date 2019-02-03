package net.apptronic.common.android.mvvm.components.activity

import net.apptronic.common.android.mvvm.components.android.AndroidLifecycle
import net.apptronic.common.core.component.Component
import net.apptronic.common.core.component.lifecycle.Lifecycle
import net.apptronic.common.core.component.lifecycle.LifecycleStageImpl

open class AndroidViewModel(lifecycle: Lifecycle) : Component(lifecycle) {

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

    fun doOnCreate(callback: LifecycleStageImpl.OnEnterHandler.() -> Unit) {
        onEnterStage(AndroidLifecycle.STAGE_CREATED, callback)
    }

    fun doOnStart(callback: LifecycleStageImpl.OnEnterHandler.() -> Unit) {
        onEnterStage(AndroidLifecycle.STAGE_STARTED, callback)
    }

    fun doOnResume(callback: LifecycleStageImpl.OnEnterHandler.() -> Unit) {
        onEnterStage(AndroidLifecycle.STAGE_RESUMED, callback)
    }

    fun doOnPause(callback: LifecycleStageImpl.OnExitHandler.() -> Unit) {
        onExitStage(AndroidLifecycle.STAGE_RESUMED, callback)
    }

    fun doOnStop(callback: LifecycleStageImpl.OnExitHandler.() -> Unit) {
        onExitStage(AndroidLifecycle.STAGE_STARTED, callback)
    }

    fun doOnDestroy(callback: LifecycleStageImpl.OnExitHandler.() -> Unit) {
        onExitStage(AndroidLifecycle.STAGE_CREATED, callback)
    }

}