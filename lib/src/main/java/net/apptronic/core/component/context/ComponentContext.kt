package net.apptronic.core.component.context

import net.apptronic.core.component.di.DependencyProvider
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.threading.ContextWorkers
import net.apptronic.core.component.threading.DefaultContextWorkers
import net.apptronic.core.component.threading.SubContextWorkers

interface ComponentContext {

    fun getLifecycle(): Lifecycle

    fun workers(): ContextWorkers

    fun objects(): DependencyProvider

}

abstract class SubContext(
    private val parent: ComponentContext
) : ComponentContext {

    private val objects = DependencyProvider(this, parent.objects())
    private val workers = SubContextWorkers(parent.workers())

    override fun workers(): ContextWorkers {
        return workers
    }

    override fun objects(): DependencyProvider {
        return objects
    }

}

class EmptyContext : ComponentContext {

    private val workers = DefaultContextWorkers()
    private val lifecycle = Lifecycle(workers)
    private val diContext = DependencyProvider(this, null)

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

    override fun workers(): ContextWorkers {
        return workers
    }

    override fun objects(): DependencyProvider {
        return diContext
    }

}
