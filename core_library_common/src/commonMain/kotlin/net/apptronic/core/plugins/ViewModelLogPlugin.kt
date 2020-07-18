package net.apptronic.core.plugins

import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.lifecycle.LifecycleStageDefinition
import net.apptronic.core.component.plugin.Plugin
import net.apptronic.core.component.plugin.pluginDescriptor
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelLifecycle

/**
 * Install plugin which enables logging of [ViewModel] lifecycle events
 */
fun Context.installViewModelLogPlugin(logMessage: (String) -> Unit) {
    installPlugin(ViewModelLogPluginDescriptor, ViewModelLogPlugin(logMessage))
}

private val ViewModelLogPluginDescriptor = pluginDescriptor<ViewModelLogPlugin>()

private class ViewModelLogPlugin internal constructor(
        private val logMessage: (String) -> Unit
) : Plugin() {

    override fun onComponent(component: Component) {
        super.onComponent(component)
        if (component is ViewModel) {
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

    private fun stateOfStage(component: Component, definition: LifecycleStageDefinition) {
        component.onEnterStage(definition) {
            logMessage("ViewModelLifecycle: $component entered stage${definition.name}")
        }
        component.onExitStage(definition) {
            logMessage("ViewModelLifecycle: $component exited stage${definition.name}")
        }
    }

}