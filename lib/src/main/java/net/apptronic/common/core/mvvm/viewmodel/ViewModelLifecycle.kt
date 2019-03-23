package net.apptronic.common.core.mvvm.viewmodel

import net.apptronic.common.core.component.lifecycle.Lifecycle
import net.apptronic.common.core.component.threading.ContextWorkers

class ViewModelLifecycle(workers: ContextWorkers) : Lifecycle(workers) {

    companion object {
        const val STAGE_CREATED = "_created"
        const val STAGE_VISIBLE = "_visible"
        const val STAGE_FOCUSED = "_focused"
    }

    val created = addStage(STAGE_CREATED)
    val visible = addStage(STAGE_VISIBLE)
    val focused = addStage(STAGE_FOCUSED)

}