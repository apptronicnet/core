package net.apptronic.core.component.context

import net.apptronic.core.component.di.DependencyProvider
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.threading.ContextWorkers
import net.apptronic.core.component.threading.DefaultContextWorkers
import net.apptronic.core.component.threading.SubContextWorkers

interface Context {

    fun getLifecycle(): Lifecycle

    fun getWorkers(): ContextWorkers

    fun getProvider(): DependencyProvider

}

abstract class SubContext(
    parent: Context
) : Context {

    init {
        parent.getLifecycle().getStage(Lifecycle.ROOT_STAGE)?.doOnExit {
            getLifecycle().terminate()
        }
    }

    private val objects = DependencyProvider(this, parent.getProvider())
    private val workers = SubContextWorkers(parent.getWorkers())

    override fun getWorkers(): ContextWorkers {
        return workers
    }

    override fun getProvider(): DependencyProvider {
        return objects
    }

}

open class BasicContext(
    parent: Context
) : SubContext(parent) {

    private val lifecycle = Lifecycle(getWorkers())

    override fun getLifecycle(): Lifecycle {
        return this.lifecycle
    }

}

class EmptyContext : Context {

    private val workers = DefaultContextWorkers()
    private val lifecycle = Lifecycle(workers)
    private val diContext = DependencyProvider(this, null)

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }

    override fun getWorkers(): ContextWorkers {
        return workers
    }

    override fun getProvider(): DependencyProvider {
        return diContext
    }

}
