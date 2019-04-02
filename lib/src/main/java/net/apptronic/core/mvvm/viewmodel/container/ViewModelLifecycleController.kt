package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.component.lifecycle.exitStage
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelLifecycle

open class ViewModelLifecycleController(
    private val viewModel: ViewModel
) {

    private fun setStage(value: Boolean, stageName: String) {
        if (value) {
            enterStage(viewModel, stageName)
        } else {
            exitStage(viewModel, stageName)
        }
    }

    fun setCreated(value: Boolean) {
        setStage(value, ViewModelLifecycle.STAGE_CREATED)
    }

    fun setBound(value: Boolean) {
        setStage(value, ViewModelLifecycle.STAGE_BOUND)
    }

    fun setVisible(value: Boolean) {
        setStage(value, ViewModelLifecycle.STAGE_VISIBLE)
    }

    fun setFocused(value: Boolean) {
        setStage(value, ViewModelLifecycle.STAGE_FOCUSED)
    }

    fun terminate() {
        viewModel.terminate()
    }

}