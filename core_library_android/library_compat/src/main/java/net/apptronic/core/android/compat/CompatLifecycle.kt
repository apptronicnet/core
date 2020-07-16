package net.apptronic.core.android.compat

import net.apptronic.core.component.lifecycle.defineLifecycle
import net.apptronic.core.component.lifecycle.lifecycleStage

val COMPAT_STAGE_CREATED = lifecycleStage("compat_created")

val CompatComponentLifecycle = defineLifecycle {
    addStage(COMPAT_STAGE_CREATED)
}