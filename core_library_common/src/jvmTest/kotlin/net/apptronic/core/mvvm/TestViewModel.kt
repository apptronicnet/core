package net.apptronic.core.mvvm

import net.apptronic.core.testutils.TestContext
import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.component.lifecycle.exitStage
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModelLifecycle

open class TestViewModel : ViewModel(
        ViewModelContext(TestContext())
) {

    fun create() {
        enterStage(this, ViewModelLifecycle.STAGE_CREATED)
    }

    fun bind() {
        enterStage(this, ViewModelLifecycle.STAGE_BOUND)
    }

    fun show() {
        enterStage(this, ViewModelLifecycle.STAGE_VISIBLE)
    }

    fun focus() {
        enterStage(this, ViewModelLifecycle.STAGE_FOCUSED)
    }

    fun unfocus() {
        exitStage(this, ViewModelLifecycle.STAGE_FOCUSED)
    }

    fun hide() {
        exitStage(this, ViewModelLifecycle.STAGE_VISIBLE)
    }

    fun unbind() {
        exitStage(this, ViewModelLifecycle.STAGE_BOUND)
    }

    fun destroy() {
        exitStage(this, ViewModelLifecycle.STAGE_CREATED)
    }

}