package net.apptronic.core.viewmodel

import net.apptronic.core.context.lifecycle.defineLifecycle
import net.apptronic.core.context.lifecycle.lifecycleStage

val ViewModelLifecycleDefinition = defineLifecycle {
    addStage(ViewModelLifecycle.Attached)
    addStage(ViewModelLifecycle.Bound)
    addStage(ViewModelLifecycle.Visible)
    addStage(ViewModelLifecycle.Focused)
}

object ViewModelLifecycle {

    /**
     * Lifecycle stage definition: [ViewModel] is attached to [ViewModelParent]
     */
    val Attached = lifecycleStage("attached")

    /**
     * Lifecycle stage definition: [ViewModel] is bound to view
     */
    val Bound = lifecycleStage("bound")

    /**
     * Lifecycle stage definition: [ViewModel] view is visible for user
     */
    val Visible = lifecycleStage("visible")

    /**
     * Lifecycle stage definition: [ViewModel] view is focused and user can interact with it
     */
    val Focused = lifecycleStage("focused")

}