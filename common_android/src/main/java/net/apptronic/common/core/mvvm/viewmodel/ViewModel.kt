package net.apptronic.common.core.mvvm.viewmodel

import net.apptronic.common.core.component.Component
import net.apptronic.common.core.component.threading.ContextWorkers
import net.apptronic.common.core.mvvm.viewmodel.adapter.ViewModelContainer
import java.util.*

open class ViewModel(context: ViewModelContext) : Component(context) {

    private val innerContainers = LinkedList<ViewModelContainer>()
    /**
     * Called to end lifecycle for this view model: lifecycle will be forced to be exited and
     * all inner models will be also finished
     */
    fun finishLifecycle() {
        innerContainers.forEach {
            it.finishAll()
        }
        getLifecycle().finish()
    }

    fun subModelContainer(): ViewModelContainer {
        return ViewModelContainer(this).also {
            innerContainers.add(it)
        }
    }

    private var parent: ViewModelParent? = null

    fun onAttachToParent(parent: ViewModelParent) {
        this.parent = parent
    }

    fun onDetachFromParent() {
        this.parent = null
    }

    fun closeSelf(transitionInfo: Any? = null): Boolean {
        return parent?.let {
            workers().execute(ContextWorkers.DEFAULT) {
                it.requestCloseSelf(this, transitionInfo)
            }
            true
        } ?: false
    }

}