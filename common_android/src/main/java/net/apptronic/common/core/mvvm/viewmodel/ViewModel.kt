package net.apptronic.common.core.mvvm.viewmodel

import net.apptronic.common.core.component.Component

open class ViewModel(context: ViewModelContext) : Component(context) {

    private val innerContainers = mutableListOf<ViewModelContainer>()
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

    internal fun onAddedToContainer(parent: ViewModelParent) {
        context.getLifecycle().enterStage(ViewModelLifecycle.STAGE_CREATED)
    }

    internal fun onRemovedFromContainer(parent: ViewModelParent) {
        context.getLifecycle().exitStage(ViewModelLifecycle.STAGE_CREATED)
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
            workers().execute(context.workers().getDefaultWorker()) {
                it.requestCloseSelf(this, transitionInfo)
            }
            true
        } ?: false
    }

}