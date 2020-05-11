package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.lifecycle.LifecycleStageDefinition
import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.component.lifecycle.exitStage
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelLifecycle

open class ViewModelLifecycleController(
    private val viewModel: ViewModel
) {

    private fun setStage(value: Boolean, definition: LifecycleStageDefinition) {
        if (value) {
            enterStage(viewModel.context, definition)
        } else {
            exitStage(viewModel.context, definition)
        }
    }

    open fun setCreated(value: Boolean) {
        setStage(value, ViewModelLifecycle.STAGE_CREATED)
    }

    open fun setBound(value: Boolean) {
        setStage(value, ViewModelLifecycle.STAGE_BOUND)
    }

    open fun setVisible(value: Boolean) {
        setStage(value, ViewModelLifecycle.STAGE_VISIBLE)
    }

    open fun setFocused(value: Boolean) {
        setStage(value, ViewModelLifecycle.STAGE_FOCUSED)
    }

    open fun terminate() {
        viewModel.context.lifecycle.terminate()
    }

}