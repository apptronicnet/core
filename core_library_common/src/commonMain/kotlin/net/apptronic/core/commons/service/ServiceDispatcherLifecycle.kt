package net.apptronic.core.commons.service

import net.apptronic.core.context.lifecycle.defineLifecycle
import net.apptronic.core.context.lifecycle.lifecycleStage

internal val STAGE_SERVICE_RUNNING = lifecycleStage("Running")

internal val ServiceDispatcherLifecycle = defineLifecycle {
    addStage(STAGE_SERVICE_RUNNING)
}