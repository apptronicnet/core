package net.apptronic.core.mvvm.viewmodel

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.SubContext
import net.apptronic.core.component.lifecycle.Lifecycle

open class ViewModelContext : SubContext {

    private val lifecycle: ViewModelLifecycle

    constructor(parent: Context) : super(parent) {
        this.lifecycle = ViewModelLifecycle(workers)
    }

    constructor(parent: Context, lifecycle: ViewModelLifecycle) : super(parent) {
        this.lifecycle = lifecycle
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

}