package net.apptronic.core.view.context

import net.apptronic.core.component.lifecycle.defineLifecycle
import net.apptronic.core.component.lifecycle.lifecycleStage

val CoreViewAttachedState = lifecycleStage("core_view_attached")

val CoreViewLifecycle = defineLifecycle {
    addStage(CoreViewAttachedState)
}