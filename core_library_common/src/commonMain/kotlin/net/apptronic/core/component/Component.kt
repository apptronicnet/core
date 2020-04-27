package net.apptronic.core.component

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.di.DependencyProvider
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.lifecycle.LifecycleStage

abstract class Component {

    abstract val context: Context

    fun provider(): DependencyProvider = context.dependencyDispatcher()

    val id: Long = ComponentRegistry.nextId()

    fun onceStage(stageName: String, key: String, action: () -> Unit) {
        getLifecycle().getStage(stageName)?.doOnce(key, action)
    }

    fun onEnterStage(stageName: String, callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        getLifecycle().getStage(stageName)?.doOnEnter(callback)
    }

    fun onExitStage(stageName: String, callback: LifecycleStage.OnExitHandler.() -> Unit) {
        getLifecycle().getStage(stageName)?.doOnExit(callback)
    }

    fun doOnTerminate(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(Lifecycle.ROOT_STAGE, callback)
    }

    open fun getLifecycle(): Lifecycle {
        return context.getLifecycle()
    }

}

fun Component.terminate() {
    context.getLifecycle().terminate()
}
