package net.apptronic.core.mvvm.viewmodel

import net.apptronic.core.component.lifecycle.defineLifecycle
import net.apptronic.core.component.lifecycle.lifecycleStage

val VIEW_MODEL_LIFECYCLE = defineLifecycle {
    addStage(ViewModelLifecycle.STAGE_ATTACHED)
    addStage(ViewModelLifecycle.STAGE_BOUND)
    addStage(ViewModelLifecycle.STAGE_VISIBLE)
    addStage(ViewModelLifecycle.STAGE_FOCUSED)
}

object ViewModelLifecycle {

    /**
     * Lifecycle stage definition: [ViewModel] is attached to [ViewModelParent]
     */
    val STAGE_ATTACHED = lifecycleStage("attached")

    /**
     * Lifecycle stage definition: [ViewModel] is bound to view
     */
    val STAGE_BOUND = lifecycleStage("bound")

    /**
     * Lifecycle stage definition: [ViewModel] view is visible for user
     */
    val STAGE_VISIBLE = lifecycleStage("visible")

    /**
     * Lifecycle stage definition: [ViewModel] view is focused and user can interact with it
     */
    val STAGE_FOCUSED = lifecycleStage("focused")

}