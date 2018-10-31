package net.apptronic.common.android.ui.components.activity

import net.apptronic.common.android.ui.viewmodel.lifecycle.Lifecycle

class ActivityLifecycle : Lifecycle() {

    val createdStage = createStage("Created")

    val startedStage = createStage("Started")

    val resumedStage = createStage("Resumed")

}