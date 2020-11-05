package net.apptronic.core.viewmodel

import net.apptronic.core.context.Context
import net.apptronic.core.context.component.IComponent
import net.apptronic.core.context.lifecycle.LifecycleStageDefinition
import net.apptronic.core.context.plugin.Plugin
import net.apptronic.core.context.plugin.pluginDescriptor

/**
 * Install plugin which enables logging of [ViewModel] lifecycle events
 */
fun Context.installViewModelLogPlugin(logMessage: (String) -> Unit) {
    installPlugin(ViewModelLogPluginDescriptor, ViewModelLogPlugin(logMessage))
}

private val ViewModelLogPluginDescriptor = pluginDescriptor<ViewModelLogPlugin>()

private class ViewModelLogPlugin internal constructor(
        private val logMessage: (String) -> Unit
) : Plugin {

    override fun onComponent(component: IComponent) {
        super.onComponent(component)
        if (component is IViewModel) {
            component.context.lifecycle.rootStage.doOnce {
                logMessage("ViewModelLifecycle: $component initialized")
            }
            component.doOnTerminate {
                logMessage("ViewModelLifecycle: ${component} terminated")
            }
            stateOfStage(component, ViewModelLifecycle.STAGE_ATTACHED)
            stateOfStage(component, ViewModelLifecycle.STAGE_BOUND)
            stateOfStage(component, ViewModelLifecycle.STAGE_VISIBLE)
            stateOfStage(component, ViewModelLifecycle.STAGE_FOCUSED)
        }
    }

    private fun stateOfStage(component: IComponent, definition: LifecycleStageDefinition) {
        component.onEnterStage(definition) {
            logMessage("ViewModelLifecycle: $component entered stage${definition.name}")
        }
        component.onExitStage(definition) {
            logMessage("ViewModelLifecycle: $component exited stage${definition.name}")
        }
    }

}