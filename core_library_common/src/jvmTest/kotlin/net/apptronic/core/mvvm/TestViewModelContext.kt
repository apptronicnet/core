package net.apptronic.core.mvvm

import net.apptronic.core.component.lifecycle.LifecycleStageDefinition
import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.component.lifecycle.exitStage
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModelLifecycle

private fun ViewModelContext.setStage(stage: LifecycleStageDefinition, value: Boolean) {
    if (value) {
        enterStage(this, stage)
    } else {
        exitStage(this, stage)
    }
}

fun ViewModelContext.setCreated(value: Boolean) {
    setStage(ViewModelLifecycle.STAGE_CREATED, value)
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