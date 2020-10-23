package net.apptronic.core.component

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.lifecycle.LifecycleStage
import net.apptronic.core.component.lifecycle.LifecycleStageDefinition
import net.apptronic.core.component.plugin.Extendable
import net.apptronic.core.component.plugin.Extensions

fun IComponent.applyPlugins() {
    context.plugins.nextComponent(this)
}

interface IComponent : Extendable, Contextual {

    val componentId: Long

    fun onceStage(definition: LifecycleStageDefinition, key: String, action: () -> Unit)

    fun onEnterStage(definition: LifecycleStageDefinition, callback: LifecycleStage.OnEnterHandler.() -> Unit)

    fun onExitStage(definition: LifecycleStageDefinition, callback: LifecycleStage.OnExitHandler.() -> Unit)

    fun doOnTerminate(callback: LifecycleStage.OnExitHandler.() -> Unit)

}

abstract class AbstractComponent : IComponent {

    final override val extensions: Extensions = Extensions()

    final override val componentId: Long = ComponentRegistry.nextId()

    final override fun onceStage(definition: LifecycleStageDefinition, key: String, action: () -> Unit) {
        context.lifecycle[definition]?.doOnce(key, action)
    }

    final override fun onEnterStage(definition: LifecycleStageDefinition, callback: LifecycleStage.OnEnterHandler.() -> Unit) {
        context.lifecycle[definition]?.doOnEnter(callback)
    }

    final override fun onExitStage(definition: LifecycleStageDefinition, callback: LifecycleStage.OnExitHandler.() -> Unit) {
        context.lifecycle[definition]?.doOnExit(callback)
    }

    final override fun doOnTerminate(callback: LifecycleStage.OnExitHandler.() -> Unit) {
        onExitStage(Lifecycle.ROOT_STAGE, callback)
    }

}

fun IComponent.terminate() {
    context.lifecycle.terminate()
}
