package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.lifecycle.LifecycleStageDefinition
import net.apptronic.core.context.lifecycle.enterStage
import net.apptronic.core.context.lifecycle.exitStage
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModelLifecycle

class ViewModelLifecycleController(
        private val viewModel: IViewModel
) {

    private fun setStage(value: Boolean, definition: LifecycleStageDefinition) {
        if (value) {
            enterStage(viewModel.context, definition)
        } else {
            exitStage(viewModel.context, definition)
        }
    }

    fun setAttached(value: Boolean) {
        setStage(value, ViewModelLifecycle.Attached)
    }

    fun setBound(value: Boolean) {
        setStage(value, ViewModelLifecycle.Bound)
    }

    fun setVisible(value: Boolean) {
        setStage(value, ViewModelLifecycle.Visible)
    }

    fun setFocused(value: Boolean) {
        setStage(value, ViewModelLifecycle.Focused)
    }

    fun terminate() {
        viewModel.context.lifecycle.terminate()
    }

}