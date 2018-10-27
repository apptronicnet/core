package net.apptronic.common.android.ui.components

import net.apptronic.common.android.ui.viewmodel.lifecycle.Lifecycle

class FragmentLifecycle : Lifecycle() {

    val createdStage = createStage("Created")

    val viewCreatedStage = createStage("ViewCreated")

    val startedStage = createStage("Started")

    val resumedStage = createStage("Resumed")

}