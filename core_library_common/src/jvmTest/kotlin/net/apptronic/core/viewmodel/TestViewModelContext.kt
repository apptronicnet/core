package net.apptronic.core.viewmodel

import net.apptronic.core.context.lifecycle.LifecycleStageDefinition
import net.apptronic.core.context.lifecycle.enterStage
import net.apptronic.core.context.lifecycle.exitStage

private fun ViewModelContext.setStage(stage: LifecycleStageDefinition, value: Boolean) {
    if (value) {
        enterStage(this, stage)
    } else {
        exitStage(this, stage)
    }
}

fun ViewModelContext.setAttached(value: Boolean) {
    setStage(ViewModelLifecycle.STAGE_ATTACHED, value)
}

fun ViewModelContext.setBound(value: Boolean) {
    setStage(ViewModelLifecycle.STAGE_BOUND, value)
}

fun ViewModelContext.setVisible(value: Boolean) {
    setStage(ViewModelLifecycle.STAGE_VISIBLE, value)
}

fun ViewModelContext.setFocused(value: Boolean) {
    setStage(ViewModelLifecycle.STAGE_FOCUSED, value)
}