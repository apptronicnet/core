package net.apptronic.common.utils

import net.apptronic.common.core.component.Component
import net.apptronic.common.core.component.lifecycle.LifecycleStage

open class BaseTestComponent() : Component(TestContext()) {

    fun getTestContext(): TestContext = context as TestContext

    override fun getLifecycle(): TestLifecycle = context.getLifecycle() as TestLifecycle

    fun doOnCreate(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        getLifecycle().created.doOnEnter(callback)
    }

    fun doOnDestroy(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        getLifecycle().created.doOnExit(callback)
    }

    fun doOnceCreated(key: String, action: () -> Unit) {
        getLifecycle().created.doOnce(key, action)
    }

    fun doOnActivated(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        getLifecycle().activated.doOnEnter(callback)
    }

    fun doOnDeactivated(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        getLifecycle().activated.doOnExit(callback)
    }

    fun doOnceActivated(key: String, action: () -> Unit) {
        getLifecycle().activated.doOnce(key, action)
    }

    fun doOnWorking(callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        getLifecycle().working.doOnEnter(callback)
    }

    fun doOnStopped(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        getLifecycle().working.doOnExit(callback)
    }

    fun doOnceWorking(key: String, action: () -> Unit) {
        getLifecycle().working.doOnce(key, action)
    }

}