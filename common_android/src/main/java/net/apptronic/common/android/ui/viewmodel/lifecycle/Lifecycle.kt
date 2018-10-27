package net.apptronic.common.android.ui.viewmodel.lifecycle

import java.util.*

open class Lifecycle {

    private val stages = LinkedList<LifecycleStage>()

    protected fun createStage(name: String): LifecycleStage {
        return LifecycleStage(name).apply { stages.add(this) }
    }

    fun getActiveStage(): LifecycleStage? {
        return stages.lastOrNull { it.isEntered() }
    }

}