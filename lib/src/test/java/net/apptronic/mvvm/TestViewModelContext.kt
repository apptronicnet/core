package net.apptronic.mvvm

import net.apptronic.common.utils.TestContext
import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.component.lifecycle.exitStage
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModelLifecycle

class TestViewModelContext : ViewModelContext(TestContext()) {

    private fun setStage(stageName: String, value: Boolean) {
        if (value) {
            enterStage(this, stageName)
        } else {
            exitStage(this, stageName)
        }
    }

    fun terminate() {
        getLifecycle().terminate()
    }

    fun setCreated(value: Boolean) {
        setStage(ViewModelLifecycle.STAGE_CREATED, value)
    }

    fun setBound(value: Boolean) {
        setStage(ViewModelLifecycle.STAGE_BOUND, value)
    }

    fun setVisible(value: Boolean) {
        setStage(ViewModelLifecycle.STAGE_VISIBLE, value)
    }

    fun setFocused(value: Boolean) {
        setStage(ViewModelLifecycle.STAGE_FOCUSED, value)
    }

}