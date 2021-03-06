package net.apptronic.core.testutils

import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.lifecycle.Lifecycle
import net.apptronic.core.context.lifecycle.LifecycleStage

open class BaseTestComponent : Component {

    constructor(contextInitializer: Context.() -> Unit = {}) : super(createTestContext(builder = contextInitializer))

    fun getLifecycle(): Lifecycle = context.lifecycle

    fun doOnCreate(callback: LifecycleStage.OnEnterHandler.() -> Unit) = onEnterStage(TestLifecycle.STAGE_CREATED, callback)

    fun doOnDestroy(callback: LifecycleStage.OnExitHandler.() -> Unit) = onExitStage(TestLifecycle.STAGE_CREATED, callback)

    fun doOnceCreated(key: String, action: () -> Unit) = onceStage(TestLifecycle.STAGE_CREATED, key, action)

    fun doOnActivated(callback: LifecycleStage.OnEnterHandler.() -> Unit) = onEnterStage(TestLifecycle.STAGE_ACTIVATED, callback)

    fun doOnDeactivated(callback: LifecycleStage.OnExitHandler.() -> Unit) = onExitStage(TestLifecycle.STAGE_ACTIVATED, callback)

    fun doOnceActivated(key: String, action: () -> Unit) = onceStage(TestLifecycle.STAGE_ACTIVATED, key, action)

    fun doOnWorking(callback: LifecycleStage.OnEnterHandler.() -> Unit) = onEnterStage(TestLifecycle.STAGE_WORKING, callback)

    fun doOnStopped(callback: LifecycleStage.OnExitHandler.() -> Unit) = onExitStage(TestLifecycle.STAGE_WORKING, callback)

    fun doOnceWorking(key: String, action: () -> Unit) = onceStage(TestLifecycle.STAGE_WORKING, key, action)

}