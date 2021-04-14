package net.apptronic.core.viewmodel

import net.apptronic.core.context.Context
import net.apptronic.core.context.lifecycle.LifecycleStageDefinition
import net.apptronic.core.context.lifecycle.enterStage
import net.apptronic.core.context.lifecycle.exitStage

private fun Context.setStage(stage: LifecycleStageDefinition, value: Boolean) {
    if (value) {
        enterStage(this, stage)
    } else {
        exitStage(this, stage)
    }
}

fun Context.setAttached(value: Boolean) {
    setStage(ViewModelLifecycle.Attached, value)
}

fun Context.setBound(value: Boolean) {
    setStage(ViewModelLifecycle.Bound, value)
}

fun Context.setVisible(value: Boolean) {
    setStage(ViewModelLifecycle.Visible, value)
}

fun Context.setFocused(value: Boolean) {
    setStage(ViewModelLifecycle.Focused, value)
}