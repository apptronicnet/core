package net.apptronic.core.component.context.local

import net.apptronic.core.component.lifecycle.Lifecycle

class LocalLifecycle internal constructor(
        parentStageName: String
) : Lifecycle() {

    init {
        addStage("Local:$parentStageName")
    }

}