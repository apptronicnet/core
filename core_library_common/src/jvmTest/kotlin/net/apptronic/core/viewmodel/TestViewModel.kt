package net.apptronic.core.viewmodel

import net.apptronic.core.context.lifecycle.enterStage
import net.apptronic.core.context.lifecycle.exitStage
import net.apptronic.core.context.viewModelContext
import net.apptronic.core.testutils.createTestContext

open class TestViewModel : ViewModel(createTestContext().viewModelContext()) {

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