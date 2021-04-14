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

    override fun onViewModelAttached(viewModel: IViewModel) {
        super.onViewModelAttached(viewModel)
        viewModel.context.lifecycle.rootStage.doOnce {
            logMessage("ViewModelLifecycle: $viewModel initialized")
        }
        viewModel.doOnTerminate {
            logMessage("ViewModelLifecycle: ${viewModel} terminated")
        }
        stateOfStage(viewModel, ViewModelLifecycle.Attached)
        stateOfStage(viewModel, ViewModelLifecycle.Bound)
        stateOfStage(viewModel, ViewModelLifecycle.Visible)
        stateOfStage(viewModel, ViewModelLifecycle.Focused)
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