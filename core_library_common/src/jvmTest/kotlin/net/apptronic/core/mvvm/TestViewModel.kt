package net.apptronic.core.mvvm

import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.component.lifecycle.exitStage
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModelLifecycle
import net.apptronic.core.testutils.TestContext

open class TestViewModel : ViewModel(
        ViewModelContext(TestContext())
) {

    fun create() {
        enterStage(context, ViewModelLifecycle.STAGE_CREATED)
    }

    fun bind() {
        enterStage(context, ViewModelLifecycle.STAGE_BOUND)
    }

    fun show() {
        enterStage(context, ViewModelLifecycle.STAGE_VISIBLE)
    }

    fun focus() {
        enterStage(context, ViewModelLifecycle.STAGE_FOCUSED)
    }

    fun unfocus() {
        exitStage(context, ViewModelLifecycle.STAGE_FOCUSED)
    }

    fun hide() {
        exitStage(context, ViewModelLifecycle.STAGE_VISIBLE)
    }

    fun unbind() {
        exitStage(context, ViewModelLifecycle.STAGE_BOUND)
    }

    fun destroy() {
        exitStage(context, ViewModelLifecycle.STAGE_CREATED)
    }

}