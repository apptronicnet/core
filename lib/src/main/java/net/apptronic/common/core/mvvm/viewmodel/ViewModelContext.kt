package net.apptronic.common.core.mvvm.viewmodel

import net.apptronic.common.core.component.ComponentContext
import net.apptronic.common.core.component.SubContext
import net.apptronic.common.core.component.lifecycle.Lifecycle

class ViewModelContext(parent: ComponentContext) : SubContext(parent) {
    private val lifecycle = ViewModelLifecycle(workers())

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

}