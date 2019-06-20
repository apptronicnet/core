package net.apptronic.core.mvvm.viewmodel

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.SubContext
import net.apptronic.core.threading.WorkerDefinition

open class ViewModelContext : SubContext {

    private var name = "ViewModelContext"

    init {
        getScheduler().setDefaultWorker(WorkerDefinition.DEFAULT)
    }

    private val lifecycle: ViewModelLifecycle

    constructor(parent: Context) : super(parent, ViewModelLifecycle()) {
        this.lifecycle = ViewModelLifecycle()
    }

    constructor(parent: Context, name: String) : super(parent, ViewModelLifecycle()) {
        this.lifecycle = ViewModelLifecycle()
        this.name = name
    }

    constructor(parent: Context, lifecycle: ViewModelLifecycle) : super(parent, lifecycle) {
        this.lifecycle = lifecycle
    }

    override fun getLifecycle(): ViewModelLifecycle {
        return lifecycle
    }

    override fun toString(): String {
        return super.toString() + "/" + name
    }

}