package net.apptronic.core.mvvm.viewmodel

import net.apptronic.core.component.context.ComponentContext
import net.apptronic.core.component.context.SubContext
import net.apptronic.core.component.lifecycle.Lifecycle

class ViewModelContext(parent: ComponentContext) : SubContext(parent) {
    private val lifecycle = ViewModelLifecycle(workers())

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

}