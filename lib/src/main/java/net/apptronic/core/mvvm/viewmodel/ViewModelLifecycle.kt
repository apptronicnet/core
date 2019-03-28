package net.apptronic.core.mvvm.viewmodel

import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.threading.ContextWorkers

class ViewModelLifecycle(workers: ContextWorkers) : Lifecycle(workers) {

    companion object {
        /**
         * Lifecycle stage definition: [ViewModel] is created and working
         */
        const val STAGE_CREATED = "_created"

        /**
         * Lifecycle stage definition: [ViewModel] is bound to view
         */
        const val STAGE_BOUND = "_bound"

        /**
         * Lifecycle stage definition: [ViewModel] view is visible for user
         */
        const val STAGE_VISIBLE = "_visible"

        /**
         * Lifecycle stage definition: [ViewModel] view is focused and user can interact with it
         */
        const val STAGE_FOCUSED = "_focused"
    }

    val created = addStage(STAGE_CREATED)
    val bound = addStage(STAGE_BOUND)
    val visible = addStage(STAGE_VISIBLE)
    val focused = addStage(STAGE_FOCUSED)

}