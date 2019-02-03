package net.apptronic.common.core.component

import net.apptronic.common.core.component.di.DIContext
import net.apptronic.common.core.component.lifecycle.Lifecycle
import net.apptronic.common.core.component.threading.ContextWorkers
import net.apptronic.common.core.component.threading.SubContextWorkers

interface ComponentContext {

    fun getLifecycle(): Lifecycle

    fun workers(): ContextWorkers

    fun objects(): DIContext

}

abstract class SubContext(
    private val parent: ComponentContext
) : ComponentContext {

    private val objects = DIContext(this, parent.objects())
    private val workers = SubContextWorkers(parent.workers())

    override fun workers(): ContextWorkers {
        return workers
    }

    override fun objects(): DIContext {
        return objects
    }

}