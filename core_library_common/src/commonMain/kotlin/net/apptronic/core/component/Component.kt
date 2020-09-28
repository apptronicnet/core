package net.apptronic.core.component

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.lifecycle.LifecycleStage
import net.apptronic.core.component.lifecycle.LifecycleStageDefinition
import net.apptronic.core.component.plugin.Extendable
import net.apptronic.core.component.plugin.Extensions

fun IComponent.applyPlugins() {
    context.plugins.plugins.forEach {
        it.onComponent(this)
    }
}

interface IComponent : Extendable, Contextual {

    val componentId: Long

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

abstract class Component : IComponent {

    final override val extensions: Extensions = Extensions()

    final override val componentId: Long = ComponentRegistry.nextId()

}

fun IComponent.terminate() {
    context.lifecycle.terminate()
}
