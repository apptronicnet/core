package net.apptronic.core.base.utils

import net.apptronic.core.component.lifecycle.Lifecycle

class TestLifecycle : Lifecycle() {

    companion object {
        const val STAGE_CREATED = "test_created";
        const val STAGE_ACTIVATED = "test_activated";
        const val STAGE_WORKING = "test_working";
    }

    val created = addStage(STAGE_CREATED)
    val activated = addStage(STAGE_ACTIVATED)
    val working = addStage(STAGE_WORKING)

}