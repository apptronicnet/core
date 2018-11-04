package net.apptronic.common.android.ui.viewmodel.lifecycle

import java.util.*

/**
 * Def
 */
open class Lifecycle {

    private val stages = LinkedList<LifecycleStage>()

    private val rootStage: LifecycleStage

    init {
        rootStage = createStage("Root").apply {
            enter()
        }
    }

    protected fun createStage(name: String): LifecycleStage {
        val stage = LifecycleStage(this, name)
        stages.add(stage)
        return stage
    }

    fun getActiveStage(): LifecycleStage {
        return stages.lastOrNull { it.isEntered() } ?: rootStage
    }

    fun exit() {
        rootStage.exit()
    }

}