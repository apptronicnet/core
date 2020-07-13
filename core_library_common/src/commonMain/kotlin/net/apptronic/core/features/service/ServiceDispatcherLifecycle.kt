package net.apptronic.core.features.service

import net.apptronic.core.component.lifecycle.defineLifecycle
import net.apptronic.core.component.lifecycle.lifecycleStage

internal val STAGE_SERVICE_RUNNING = lifecycleStage("Running")

internal val ServiceDispatcherLifecycle = defineLifecycle {
    addStage(STAGE_SERVICE_RUNNING)
}