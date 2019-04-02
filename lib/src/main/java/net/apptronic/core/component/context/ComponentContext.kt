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
    parent: ComponentContext
) : ComponentContext {

    init {
        parent.getLifecycle().getStage(Lifecycle.ROOT_STAGE)?.doOnExit {
            getLifecycle().terminate()
        }
    }

    private val objects = DependencyProvider(this, parent.objects())
    private val workers = SubContextWorkers(parent.workers())

    override fun workers(): ContextWorkers {
        return workers
    }

    override fun objects(): DependencyProvider {
        return objects
    }

}

open class BasicContext(
    parent: ComponentContext
) : SubContext(parent) {

    private val lifecycle = Lifecycle(workers())

    override fun getLifecycle(): Lifecycle {
        return this.lifecycle
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
