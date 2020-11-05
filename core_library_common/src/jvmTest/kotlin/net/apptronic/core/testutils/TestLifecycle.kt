package net.apptronic.core.testutils

import net.apptronic.core.context.lifecycle.defineLifecycle
import net.apptronic.core.context.lifecycle.lifecycleStage

val TEST_LIFECYCLE = defineLifecycle {
    addStage(TestLifecycle.STAGE_CREATED)
    addStage(TestLifecycle.STAGE_ACTIVATED)
    addStage(TestLifecycle.STAGE_WORKING)
}

object TestLifecycle {

    val STAGE_CREATED = lifecycleStage("test_created")
    val STAGE_ACTIVATED = lifecycleStage("test_activated")
    val STAGE_WORKING = lifecycleStage("test_working")

}