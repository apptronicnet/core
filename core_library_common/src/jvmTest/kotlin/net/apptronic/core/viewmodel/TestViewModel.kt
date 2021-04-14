package net.apptronic.core.viewmodel

import net.apptronic.core.context.childContext
import net.apptronic.core.context.lifecycle.enterStage
import net.apptronic.core.context.lifecycle.exitStage
import net.apptronic.core.testutils.createTestContext

open class TestViewModel : ViewModel(createTestContext().childContext()) {

    fun attach() {
        enterStage(context, ViewModelLifecycle.Attached)
    }

    fun bind() {
        enterStage(context, ViewModelLifecycle.Bound)
    }

    fun show() {
        enterStage(context, ViewModelLifecycle.Visible)
    }

    fun focus() {
        enterStage(context, ViewModelLifecycle.Focused)
    }

    fun unfocus() {
        exitStage(context, ViewModelLifecycle.Focused)
    }

    fun hide() {
        exitStage(context, ViewModelLifecycle.Visible)
    }

    fun unbind() {
        exitStage(context, ViewModelLifecycle.Bound)
    }

    fun detach() {
        exitStage(context, ViewModelLifecycle.Attached)
    }

}