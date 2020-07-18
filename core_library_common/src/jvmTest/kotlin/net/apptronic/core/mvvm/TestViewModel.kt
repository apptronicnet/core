package net.apptronic.core.mvvm

import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.component.lifecycle.exitStage
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelLifecycle
import net.apptronic.core.testutils.testContext

open class TestViewModel : ViewModel(testContext().viewModelContext()) {

    fun attach() {
        enterStage(context, ViewModelLifecycle.STAGE_ATTACHED)
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

    fun detach() {
        exitStage(context, ViewModelLifecycle.STAGE_ATTACHED)
    }

}