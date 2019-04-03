package net.apptronic.core.mvvm.viewmodel

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.SubContext
import net.apptronic.core.component.lifecycle.Lifecycle

open class ViewModelContext(parent: Context) : SubContext(parent) {

    private val lifecycle = ViewModelLifecycle(getWorkers())

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

}