package net.apptronic.core.mvvm.viewmodel

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.SubContext
import net.apptronic.core.threading.Scheduler

open class ViewModelContext : SubContext {

    init {
        getScheduler().setDefaultWorker(Scheduler.UI)
    }

    private val lifecycle: ViewModelLifecycle

    constructor(parent: Context) : super(parent, ViewModelLifecycle()) {
        this.lifecycle = ViewModelLifecycle()
    }

    constructor(parent: Context, lifecycle: ViewModelLifecycle) : super(parent, lifecycle) {
        this.lifecycle = lifecycle
    }

    override fun getLifecycle(): ViewModelLifecycle {
        return lifecycle
    }

}