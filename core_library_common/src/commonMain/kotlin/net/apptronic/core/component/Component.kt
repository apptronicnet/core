package net.apptronic.core.component

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.di.DependencyProvider
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.lifecycle.LifecycleStage
import net.apptronic.core.component.lifecycle.LifecycleStageDefinition
import net.apptronic.core.component.plugin.Extendable
import net.apptronic.core.component.plugin.Extensions

fun Component.applyPlugins() {
    context.plugins.plugins.forEach {
        it.onComponent(this)
    }
}

abstract class Component : Extendable, Contextual {

    override val extensions: Extensions = Extensions()

    abstract override val context: Context

    fun provider(): DependencyProvider = context.dependencyDispatcher

    val id: Long = ComponentRegistry.nextId()

    fun onceStage(definition: LifecycleStageDefinition, key: String, action: () -> Unit) {
        context.lifecycle[definition]?.doOnce(key, action)
    }

    fun onEnterStage(definition: LifecycleStageDefinition, callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        context.lifecycle[definition]?.doOnEnter(callback)
    }

    fun onExitStage(definition: LifecycleStageDefinition, callback: LifecycleStage.OnExitHandler.() -> Unit) {
        context.lifecycle[definition]?.doOnExit(callback)
    }

    fun doOnTerminate(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(Lifecycle.ROOT_STAGE, callback)
    }

}

fun Component.terminate() {
    context.lifecycle.terminate()
}
